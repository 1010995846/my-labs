package cn.cidea.framework.mq.redis.config;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessageListener;
import cn.cidea.framework.mq.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redis.RedisMQTemplate;
import cn.cidea.framework.redis.config.CIdeaRedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 消息队列配置类
 *
 * @author Charlotte
 */
@Configuration
@AutoConfigureAfter(CIdeaRedisAutoConfiguration.class)
@Slf4j
public class CIdeaRedisMQAutoConfiguration {

    @Bean
    public MQTemplate redisMQTemplate(RedissonClient redissonClient,
                                      List<MessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redissonClient);

        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);

        return redisMQTemplate;
    }

    @Configuration
    public static class ListenerAutoConfiguration {

        public ListenerAutoConfiguration(RedissonClient redissonClient, List<AbstractChannelMessageListener<?>> listeners) {
            // 添加监听器
            listeners.forEach(listener -> {
                RTopic topic = redissonClient.getTopic(listener.getChannel());
                topic.addListener(String.class, listener);
                // Class messageType = listener.getMessageType();
                // topic.addListener(messageType, listener);
                log.info("[redisMessageListenerContainer][注册 Channel({}) 对应的监听器({})]",
                        listener.getChannel(), listener.getClass().getName());
            });
        }
    }

}
