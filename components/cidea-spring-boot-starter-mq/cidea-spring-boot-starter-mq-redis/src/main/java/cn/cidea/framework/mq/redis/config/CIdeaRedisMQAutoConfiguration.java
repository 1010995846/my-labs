package cn.cidea.framework.mq.redis.config;

import cn.cidea.framework.mq.redis.core.message.pubsub.AbstractChannelMessageListener;
import cn.cidea.framework.mq.redis.RedisMQTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

/**
 * 消息队列配置类
 *
 * @author Charlotte
 */
@Slf4j
public class CIdeaRedisMQAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate() {
        RedisMQTemplate mqTemplate = new RedisMQTemplate();
        return mqTemplate;
    }

    @Bean
    public RedisMessageListenerContainer ListenerAutoConfiguration(RedisMQTemplate mqTemplate, List<AbstractChannelMessageListener<?>> listeners) {
        // 创建 RedisMessageListenerContainer 对象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置 RedisConnection 工厂。
        container.setConnectionFactory(mqTemplate.getRedisTemplate().getRequiredConnectionFactory());
        // 添加监听器
        listeners.forEach(listener -> {
            container.addMessageListener(listener, new ChannelTopic(listener.getChannel()));
            log.info("[redisMessageListenerContainer][注册 Channel({}) 对应的监听器({})]",
                    listener.getChannel(), listener.getClass().getName());
        });
        return container;
    }

}
