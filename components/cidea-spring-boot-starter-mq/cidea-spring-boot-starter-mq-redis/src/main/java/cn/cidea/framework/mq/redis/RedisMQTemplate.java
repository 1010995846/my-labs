package cn.cidea.framework.mq.redis;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis MQ 操作模板类
 *
 * @author 芋道源码
 */
@AllArgsConstructor
public class RedisMQTemplate extends MQTemplate<AbstractChannelMessage> {

    @Getter
    private final RedisTemplate<String, ?> redisTemplate;

    @Override
    protected void doSend(AbstractChannelMessage message) {
        redisTemplate.convertAndSend(message.getChannel(), JSONObject.toJSONString(message));
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
