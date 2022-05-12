package cn.cidea.framework.mq.redisson.config;

import cn.cidea.framework.mq.redisson.plugin.interceptor.MessageInitInterceptor;
import cn.cidea.framework.mq.redisson.plugin.interceptor.MessageRetryInterceptor;
import cn.cidea.framework.mq.redisson.properties.RedisMqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列自带功能配置类
 * @author Charlotte
 */
@Configuration
public class RedissonMqPluginAutoConfiguration {

    @Bean
    public MessageInitInterceptor idInterceptor(){
        return new MessageInitInterceptor();
    }

    @Bean
    public MessageRetryInterceptor retryInterceptor(){
        return new MessageRetryInterceptor();
    }

}
