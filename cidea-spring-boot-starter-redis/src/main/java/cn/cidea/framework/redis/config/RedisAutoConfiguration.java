package cn.cidea.framework.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置类
 */
@Configuration
@Slf4j
public class RedisAutoConfiguration {

    /**
     * 创建 RedisTemplate Bean，使用 JSON 序列化方式
     */    // @Bean
    // public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    //     // 创建 RedisTemplate 对象
    //     RedisTemplate<String, Object> template = new RedisTemplate<>();
    //     // 设置 RedisConnection 工厂。
    //     template.setConnectionFactory(factory);
    //     // 使用 String 序列化方式，序列化 KEY 。
    //     template.setKeySerializer(RedisSerializer.string());
    //     template.setHashKeySerializer(RedisSerializer.string());
    //     // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
    //     template.setValueSerializer(RedisSerializer.json());
    //     template.setHashValueSerializer(RedisSerializer.json());
    //     return template;
    // }

}
