package cn.cidea.framework.mq.redisson.config;

import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.framework.mq.redisson.core.message.AbstractMessageListener;
import cn.cidea.framework.mq.redisson.core.RedisChannelDispatch;
import cn.cidea.framework.mq.redisson.core.message.pubsub.AbstractPubSubListener;
import cn.cidea.framework.mq.redisson.properties.RedisMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 消息队列核心配置类
 *
 * @author Charlotte
 */
@Slf4j
@Configuration
public class RedissonMqAutoConfiguration {

    /**
     * 初始化MQTemplate
     */
    @Bean
    public RedisMQTemplate redisMQTemplate() {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate();
        return redisMQTemplate;
    }

    /**
     * 普通推送使用出栈
     * @param listeners
     * @return
     */
    @Bean
    public RedisChannelDispatch Dispatch(List<AbstractMessageListener<?>> listeners){
        // TODO pub/sub模式的监听器要和推送模式监听器分开，目前使用Class区分，看看有没有更好的方案
        return new RedisChannelDispatch(listeners);
    }

    @Bean
    public RedisMqProperties redisMqProperties() {
        return new RedisMqProperties();
    }

    /**
     * pub/sub使用监听
     */
    @Configuration
    public static class PubSubAutoConfiguration {

        public PubSubAutoConfiguration(RedissonClient redissonClient, List<AbstractPubSubListener<?>> listeners) {
            // 添加pubsub监听器
            listeners.forEach(listener -> {
                RTopic topic = redissonClient.getTopic(listener.getChannelName());
                topic.addListener(String.class, listener);
                // Class messageType = listener.getMessageType();
                // topic.addListener(messageType, listener);
                log.info("[redisMessageListenerContainer][注册 Channel({}) 对应的监听器({})]",
                        listener.getChannelName(), listener.getClass().getName());
            });
        }
    }

}
