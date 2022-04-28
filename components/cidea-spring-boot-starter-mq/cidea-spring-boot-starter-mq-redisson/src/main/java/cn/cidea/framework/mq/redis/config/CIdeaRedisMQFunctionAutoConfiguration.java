package cn.cidea.framework.mq.redis.config;

import cn.cidea.framework.mq.redis.interceptor.MessageInitInterceptor;
import cn.cidea.framework.mq.redis.interceptor.MessageRetryInterceptor;
import cn.cidea.framework.mq.redis.properties.RedisMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列自带功能配置类
 * @author Charlotte
 */
@Slf4j
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
