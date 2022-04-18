package cn.cidea.framework.mq.redis;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * Redis MQ 操作模板类
 */
@AllArgsConstructor
public class RedisMQTemplate extends MQTemplate<AbstractChannelMessage> {

    @Getter
    private final RedissonClient redissonClient;

    /**
     * {@link cn.cidea.framework.mq.redis.config.CIdeaRedisMQAutoConfiguration#redisMessageListenerContainer(RedisMQTemplate, List)}
     * @param message
     */
    @Override
    protected void doSend(AbstractChannelMessage message) {
        RTopic topic = redissonClient.getTopic(message.getChannel());
        // TODO 直接使用对象会有类加载不一致问题，原因不明，暂时使用JSONString
        topic.publish(JSONObject.toJSONString(message));
        // topic.publish(message);
    }

    /**
     * 发送 Redis 消息，基于 Redis Stream 实现
     *
     * @param message 消息
     * @return 消息记录的编号对象
     */
    // public <T extends AbstractStreamMessage> RecordId send(T message) {
    //     try {
    //         sendMessageBefore(message);
    //         // 发送消息
    //         return redisTemplate.opsForStream().add(StreamRecords.newRecord()
    //                 // 设置内容
    //                 .ofObject(JSONObject.toJSONString(message))
    //                 // 设置 stream key
    //                 .withStreamKey(message.getStreamKey()));
    //     } finally {
    //         sendMessageAfter(message);
    //     }
    // }


}
