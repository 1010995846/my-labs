package cn.cidea.framework.mq.redis.core.message.pubsub;

import cn.cidea.framework.mq.redis.core.message.AbstractMessage;
import cn.cidea.framework.mq.redis.core.message.AbstractMessageListener;
import lombok.SneakyThrows;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.protocol.pubsub.PubSubType;

/**
 * Redis Pub/Sub 监听器抽象类，用于实现广播消费
 *
 * @author Charlotte
 * @param <T> 监听的消息类型
 */
public abstract class AbstractPubSubListener<T extends AbstractMessage> extends AbstractMessageListener<T> implements RedisPubSubListener<String> {

    @SneakyThrows
    protected AbstractPubSubListener() {
        super();
    }

    @Override
    public boolean onStatus(PubSubType type, CharSequence channel) {
        return false;
    }

    @Override
    public void onPatternMessage(CharSequence pattern, CharSequence channel, String message) {
        onMessage(channel, message);
    }

}
