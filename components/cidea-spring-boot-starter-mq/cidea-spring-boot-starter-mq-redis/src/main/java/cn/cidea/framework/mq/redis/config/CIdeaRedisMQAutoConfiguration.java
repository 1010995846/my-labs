package cn.cidea.framework.mq.redis.config;

import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessageListener;
import cn.cidea.framework.mq.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redis.RedisMQTemplate;
import cn.cidea.framework.redis.config.CIdeaRedisAutoConfiguration;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;
import java.util.Properties;

/**
 * 消息队列配置类
 */
@Configuration
@AutoConfigureAfter(CIdeaRedisAutoConfiguration.class)
@Slf4j
public class CIdeaRedisMQAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
                                           List<MessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);
        return redisMQTemplate;
    }

    // ========== 消费者相关 ==========

    /**
     * 创建 Redis Pub/Sub 广播消费的容器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisMQTemplate redisMQTemplate, List<AbstractChannelMessageListener<?>> listeners) {
        // 创建 RedisMessageListenerContainer 对象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置 RedisConnection 工厂。
        container.setConnectionFactory(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory());
        // 添加监听器
        listeners.forEach(listener -> {
            listener.setMqTemplate(redisMQTemplate);
            container.addMessageListener(listener, new ChannelTopic(listener.getChannel()));
            log.info("[redisMessageListenerContainer][注册 Channel({}) 对应的监听器({})]",
                    listener.getChannel(), listener.getClass().getName());
        });
        return container;
    }

    /**
     * 创建 Redis Stream 集群消费的容器
     *
     * Redis Stream 的 xreadgroup 命令：https://www.geek-book.com/src/docs/redis/redis/redis.io/commands/xreadgroup.html
     */
    // @Bean(initMethod = "start", destroyMethod = "stop")
    // public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisStreamMessageListenerContainer(
    //         RedisMQTemplate redisMQTemplate, List<AbstractStreamMessageListener<?>> listeners) {
    //     RedisTemplate<String, ?> redisTemplate = redisMQTemplate.getRedisTemplate();
    //     checkRedisVersion(redisTemplate);
    //     // 第一步，创建 StreamMessageListenerContainer 容器
    //     // 创建 options 配置
    //     StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> containerOptions =
    //             StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
    //                     // 一次性最多拉取多少条消息
    //                     .batchSize(10)
    //                     // 目标类型。统一使用 String，通过自己封装的 AbstractStreamMessageListener 去反序列化
    //                     .targetType(String.class)
    //                     .build();
    //     // 创建 container 对象
    //     StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
    //             // StreamMessageListenerContainer.create(redisTemplate.getRequiredConnectionFactory(), containerOptions);
    //             // 使用自定义的container
    //             DefaultStreamMessageListenerContainerX.create(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory(), containerOptions);
    //
    //     // 第二步，注册监听器，消费对应的 Stream 主题
    //     String consumerName = buildConsumerName();
    //     listeners.parallelStream().forEach(listener -> {
    //         // 创建 listener 对应的消费者分组
    //         try {
    //             redisTemplate.opsForStream().createGroup(listener.getStreamKey(), listener.getGroup());
    //         } catch (Exception ignore) {}
    //         // 设置 listener 对应的 redisTemplate
    //         listener.setRedisMQTemplate(redisMQTemplate);
    //         // 创建 Consumer 对象
    //         Consumer consumer = Consumer.from(listener.getGroup(), consumerName);
    //         // 设置 Consumer 消费进度，以最小消费进度为准
    //         StreamOffset<String> streamOffset = StreamOffset.create(listener.getStreamKey(), ReadOffset.lastConsumed());
    //         // 设置 Consumer 监听
    //         StreamMessageListenerContainer.StreamReadRequestBuilder<String> builder = StreamMessageListenerContainer.StreamReadRequest
    //                 .builder(streamOffset).consumer(consumer)
    //                 // 不自动 ack
    //                 .autoAcknowledge(false)
    //                 // 默认配置，发生异常就取消消费，显然不符合预期；因此，我们设置为 false
    //                 .cancelOnError(throwable -> false);
    //         container.register(builder.build(), listener);
    //     });
    //     return container;
    // }

    /**
     * 构建消费者名字，使用本地 IP + 进程编号的方式。
     * 参考自 RocketMQ clientId 的实现
     *
     * @return 消费者名字
     */
    private static String buildConsumerName() {
        return String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
    }

    /**
     * 校验 Redis 版本号，是否满足最低的版本号要求！
     */
    private static void checkRedisVersion(RedisTemplate<String, ?> redisTemplate) {
        // 获得 Redis 版本
        Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
        String version = MapUtil.getStr(info, "redis_version");
        // 校验最低版本必须大于等于 5.0.0
        int majorVersion = Integer.parseInt(StrUtil.subBefore(version, '.', false));
        if (majorVersion < 5) {
            throw new IllegalStateException(StrUtil.format("您当前的 Redis 版本为 {}，小于最低要求的 5.0.0 版本！", version));
        }
    }

}