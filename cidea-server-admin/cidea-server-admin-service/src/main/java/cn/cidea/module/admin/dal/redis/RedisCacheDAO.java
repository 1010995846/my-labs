package cn.cidea.module.admin.dal.redis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Slf4j
public abstract class RedisCacheDAO<M extends BaseMapper<E>, E extends Model<E>> implements ICacheDAO<E> {

    @Autowired
    private M mapper;
    @Autowired
    private RedissonClient redissonClient;

    protected abstract String getKey();

    private RBloomFilter<Serializable> getBloomFilter() {
        String bloomKey = "bloom:" + getKey();
        RBloomFilter<Serializable> bloomFilter = redissonClient.getBloomFilter(bloomKey);
        bloomFilter.tryInit(1000, 0.001);
        return bloomFilter;
    }

    private String getLockKey() {
        return "lock:" + getKey();
    }

    @Override
    public void insert(E entity) {
        RLock lock = redissonClient.getLock(getLockKey() + ":" + entity.pkVal());

        try {
            lock.tryLock(1, TimeUnit.SECONDS);
            try {
                RMap<Serializable, E> cache = getCache();
                cache.put(entity.pkVal(), entity);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO
        }
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000L)
    public void refresh() {
        // 全局读写锁。写单个数据时使用读锁，互相不冲突；写全部时使用写锁，完全排它
        RLock lock = redissonClient.getReadWriteLock(getLockKey()).writeLock();
        lock.lock();
        try {
            RMap<Serializable, E> cache = getCache();
            // TODO redisson文档，各个对象的使用说明
            // redissonClient.getMap();
            // redissonClient.getMapCache();
            // redissonClient.getLocalCachedMap();
            // RBucket<Object> bucket = redissonClient.getBucket();
            List<E> list = mapper.selectList(new QueryWrapper<>());
            cache.putAll(list.stream().collect(Collectors.toMap(Model::pkVal, u -> u)));
            RBloomFilter<Serializable> bloomFilter = getBloomFilter();
            list.forEach(e -> bloomFilter.add(e.pkVal()));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E selectById(Serializable id) {
        // bloom性质：hash位图。若不存在，则一定不存在；若存在，可能hash重复导致误判
        RBloomFilter<Serializable> bloomFilter = getBloomFilter();
        if (!bloomFilter.contains(id)) {
            // 不存在立即返回，避免访问数据库，造成穿透
            return null;
        }
        RMap<Serializable, E> cache = getCache();
        E entity = cache.get(id);
        if (entity == null) {
            // 避免hash误判，小概率事件
            RLock globalLock = redissonClient.getReadWriteLock(getLockKey()).readLock();
            globalLock.lock(1, TimeUnit.SECONDS);
            RLock lock = redissonClient.getLock(getLockKey() + ":" + id);
            // redissonClient.getReadWriteLock(getLockKey());
            // redissonClient.getFairLock(getLockKey());

            try {
                lock.tryLock(1, TimeUnit.SECONDS);
                entity = cache.get(id);
                if (entity == null) {
                    // 保证同时只有一个线程查库，避免击穿
                    entity = mapper.selectById(id);
                    if (entity != null) {
                        // 写入缓存和bloom
                        cache.put(id, entity);
                        bloomFilter.add(id);
                    }
                }
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
            } finally {
                lock.unlock();
                globalLock.unlock();
            }
        }
        return entity;
    }

    @Override
    public Collection<E> selectBatchIds(Set<Serializable> ids) {
        RBloomFilter<Serializable> bloomFilter = getBloomFilter();
        ids = ids.stream()
                .filter(bloomFilter::contains)
                .collect(Collectors.toSet());
        if (ids.size() == 0) {
            return new ArrayList<>(0);
        }

        RMap<Serializable, E> cache = getCache();
        Map<Serializable, E> map = cache.getAll(new HashSet<>(ids));
        ids.removeAll(map.keySet());

        if (!ids.isEmpty()) {
            RLock globalLock = redissonClient.getReadWriteLock(getLockKey()).readLock();
            globalLock.lock(1, TimeUnit.SECONDS);

            List<RLock> lockList = ids.stream()
                    .map(id -> redissonClient.getLock(getLockKey() + ":" + id))
                    .collect(Collectors.toList());
            RLock lock = redissonClient.getMultiLock(lockList.toArray(new RLock[0]));
            // redissonClient.getRedLock(lockList.toArray(new RLock[0]));

            try {
                lock.tryLock(1, TimeUnit.SECONDS);
                Map<Serializable, E> againMap = cache.getAll(ids);
                if(!againMap.isEmpty()) {
                    map.putAll(againMap);
                    ids.removeAll(againMap.keySet());
                }
                if (!ids.isEmpty()) {
                    List<E> list = mapper.selectBatchIds(ids);
                    if (list.size() != 0) {
                        for (E entity : list) {
                            // 写入缓存和bloom
                            map.put(entity.pkVal(), entity);
                            cache.put(entity.pkVal(), entity);
                            bloomFilter.add(entity.pkVal());
                        }
                    }
                }
            } catch (InterruptedException e) {
                log.error("lock error", e);
            } finally {
                if(lock.isLocked()){
                    lock.unlock();
                }
                if(globalLock.isLocked()){
                    globalLock.unlock();
                }
            }
        }
        return map.values();
    }

    @Override
    public void deleteById(Serializable id){
        executeIfTransactional(() -> {
            // bloom好像没有删除的概念？
            // 可能是因为hash冲突的原因，一个数据对应一个hash，但hash可能对应复数数据，如果删除了这个数据对应的hash，就会导致hash对应的其它数据被误判不存在，无法确保bloom不存在则必定不存在的性质
            // RBloomFilter<Serializable> bloomFilter = redissonClient.getBloomFilter(getBloomKey());
            RMap<Serializable, E> cache = getCache();
            cache.remove(id);
        });
    }

    private void executeIfTransactional(Runnable runnable) {
        if(TransactionSynchronizationManager.isSynchronizationActive()){
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    @Override
    public void deleteBatchIds(Collection<Serializable> ids){
        executeIfTransactional(() -> {
            RMap<Serializable, E> cache = getCache();
            ids.forEach(cache::remove);
        });
    }

    private RMap<Serializable, E> getCache() {
        // TODO 若redis挂掉则改用本地缓存，避免雪崩
        String cacheKey = "cache:" + getKey();
        return redissonClient.getMap(cacheKey);
    }
}
