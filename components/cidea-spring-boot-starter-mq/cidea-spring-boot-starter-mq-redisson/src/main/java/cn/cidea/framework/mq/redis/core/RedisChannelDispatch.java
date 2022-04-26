package cn.cidea.framework.mq.redis.core;

import cn.cidea.framework.mq.redis.core.message.AbstractMessageListener;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisPubSubListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 监听队列并分派
 * 抢占模式
 * @author Charlotte
 */
@Slf4j
public class RedisChannelDispatch implements InitializingBean {

    @Autowired
    private RedissonClient redissonClient;

    private Multimap<String, AbstractMessageListener<?>> listenerMap;

    private Thread thread;

    public RedisChannelDispatch(List<AbstractMessageListener<?>> listeners) {
        if (listeners == null) {
            listeners = Collections.EMPTY_LIST;
        }
        // 过滤掉pubsub
        listeners = listeners.stream().filter(l -> !(l instanceof RedisPubSubListener)).collect(Collectors.toList());;
        ImmutableMultimap.Builder<String, AbstractMessageListener<?>> builder = ImmutableMultimap.builder();
        for (AbstractMessageListener<?> listener : listeners) {
            builder.put(listener.getChannelName(), listener);
        }
        listenerMap = builder.build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        run();
    }

    public void run() {
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    // 是否需要
                    for (Map.Entry<String, Collection<AbstractMessageListener<?>>> entry : listenerMap.asMap().entrySet()) {
                        try {
                            // 和pubsub使用同一个key会有冲突
                            RQueue<String> queue = redissonClient.getQueue(entry.getKey());
                            String jsonStr = queue.poll();
                            if (jsonStr != null) {
                                for (AbstractMessageListener<?> listener : entry.getValue()) {
                                    listener.onMessage(entry.getKey(), jsonStr);
                                }
                            }
                        } catch (Throwable t) {
                            if (t instanceof RedissonShutdownException) {
                                return;
                            }
                            log.error("监听异常", t);
                        }
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}
