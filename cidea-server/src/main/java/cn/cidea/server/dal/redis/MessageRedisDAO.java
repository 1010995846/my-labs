package cn.cidea.server.dal.redis;

import cn.cidea.server.dataobject.entity.SysMessage;
import cn.cidea.server.service.ISysMessageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息持久化的缓存层
 * @author Charlotte
 */
@Slf4j
@Component
public class MessageRedisDAO {

    @Autowired
    private ISysMessageService messageService;

    @Autowired
    private RedissonClient redissonClient;

    private static final long CACHE_REFRESH_SIZE = 5;
    private static final long CACHE_SCHEDULER_REFRESH_PERIOD = 10 * 1000L;

    private static final String BLOOM_FILTER_NAME = "mq:redis:exist:filter";
    private static final String CACHE_NAME = "mq:redis:cache";
    private static final String CACHE_LOCK_NAME = "mq:redis:lock";

    @Scheduled(fixedDelay = CACHE_SCHEDULER_REFRESH_PERIOD, initialDelay = CACHE_SCHEDULER_REFRESH_PERIOD)
    public void schedulePeriodicRefresh() {
        log.info("定时flush");
        flush();
    }

    public void write(SysMessage message) {
        RMapCache<Long, SysMessage> cache = getMapCache();
        RLock readLock = getLock().readLock();
        try {
            readLock.lock();
            cache.put(message.getId(), message);
        } finally {
            if (readLock.isLocked()) {
                readLock.unlock();
            }
        }
        if(cache.size() > CACHE_REFRESH_SIZE){
            log.info("触发缓存容量flush");
            flush();
        }
    }

    public void flush() {
        RMapCache<Long, SysMessage> map = getMapCache();
        // flush时完全排它
        RLock writeLock = getLock().writeLock();
        log.info("[Redis][MQ][Cache]flush");
        try {
            writeLock.lock();
            Map<Long, SysMessage> messageMap = map.readAllMap();
            if(messageMap.size() == 0){
                return;
            }
            map.clear();
            List<SysMessage> saveList = new ArrayList<>();
            List<SysMessage> updateList = new ArrayList<>();
            RBloomFilter<Long> filter = getBloomFilter();
            messageMap.values().forEach(message -> {
                if(filter.contains(message.getId())){
                    // 命中代表需要更新
                    updateList.add(message);
                } else {
                    // 未命中，插入，并存入过滤器，标识需要更新
                    saveList.add(message);
                    filter.add(message.getId());
                }
            });

            messageService.saveBatch(saveList);
            messageService.updateBatchById(updateList);
        } finally {
            if (writeLock.isLocked()) {
                writeLock.unlock();
            }
        }
    }

    private RMapCache<Long, SysMessage> getMapCache() {
        return redissonClient.getMapCache(CACHE_NAME);
    }

    private RBloomFilter<Long> getBloomFilter() {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
        boolean init = bloomFilter.tryInit(55000000L, 0.03);
        return bloomFilter;
    }

    public RReadWriteLock getLock() {
        return redissonClient.getReadWriteLock(CACHE_LOCK_NAME);
    }
}
