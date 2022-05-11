package cn.cidea.framework.mq.redis.config;

import cn.cidea.framework.mq.redis.plugin.interceptor.MessageInitInterceptor;
import cn.cidea.framework.mq.redis.plugin.interceptor.MessageRetryInterceptor;
import cn.cidea.framework.mq.redis.properties.RedisMqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列自带功能配置类
 * @author Charlotte
 */
@Configuration
public class CIdeaRedisMQFunctionAutoConfiguration {

    @Bean
    public RedisMqProperties redisMqProperties() {
        return new RedisMqProperties();
    }

    @Bean
    public MessageInitInterceptor idInterceptor(){
        return new MessageInitInterceptor();
    }

    @Bean
    public MessageRetryInterceptor retryInterceptor(){
        return new MessageRetryInterceptor();
    }

}
