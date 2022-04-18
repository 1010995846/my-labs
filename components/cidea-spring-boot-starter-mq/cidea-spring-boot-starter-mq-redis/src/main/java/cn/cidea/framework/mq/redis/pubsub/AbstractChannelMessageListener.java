package cn.cidea.framework.mq.redis.pubsub;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.framework.mq.core.dto.AbstractMessage;
import cn.cidea.framework.mq.core.interceptor.MessageInterceptor;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.redisson.PubSubMessageListener;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.protocol.pubsub.PubSubType;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Redis Pub/Sub 监听器抽象类，用于实现广播消费
 *
 * @param <T> 消息类型。一定要填写噢，不然会报错
 *
 * @author 芋道源码
 */
public abstract class AbstractChannelMessageListener<T extends AbstractChannelMessage> implements RedisPubSubListener<String> {

    /**
     * 消息类型
     */
    @Getter
    private final Class<T> messageType;
    /**
     * Redis Channel
     */
    private final String channel;
    /**
     * RedisMQTemplate
     */
    @Setter
    private MQTemplate mqTemplate;

    @SneakyThrows
    protected AbstractChannelMessageListener() {
        this.messageType = getMessageClass();
        this.channel = messageType.newInstance().getChannel();
    }

    /**
     * 获得 Sub 订阅的 Redis Channel 通道
     * @return channel
     */
    public final String getChannel() {
        return channel;
    }

    @Override
    public boolean onStatus(PubSubType type, CharSequence channel) {
        return false;
    }

    @Override
    public void onPatternMessage(CharSequence pattern, CharSequence channel, String message) {
        onMessage(channel, message);
    }

    @Override
    public void onMessage(CharSequence channel, String msg) {
        if(!channel.equals(getChannel())){
            return;
        }
        T messageObj = JSONObject.parseObject(msg, messageType);
        try {
            consumeMessageBefore(messageObj);
            // 消费消息
            this.onMessage(messageObj);
        } catch (RuntimeException e){
            consumeMessageAfterError(messageObj, e);
            throw e;
        } finally {
            consumeMessageAfter(messageObj);
        }
    }

    /**
     * 处理消息
     *
     * @param message 消息
     */
    public abstract void onMessage(T message);

    /**
     * 通过解析类上的泛型，获得消息类型
     *
     * @return 消息类型
     */
    @SuppressWarnings("unchecked")
    private Class<T> getMessageClass() {
        Type type = TypeUtil.getTypeArgument(getClass(), 0);
        if (type == null) {
            throw new IllegalStateException(String.format("类型(%s) 需要设置消息类型", getClass().getName()));
        }
        return (Class<T>) type;
    }

    private void consumeMessageBefore(AbstractMessage message) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 正序
        interceptors.forEach(interceptor -> interceptor.consumeMessageBefore(message));
    }

    private void consumeMessageAfter(AbstractMessage message) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).consumeMessageAfter(message);
        }
    }

    private void consumeMessageAfterError(AbstractMessage message, RuntimeException e) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).consumeMessageAfterError(message, e);
        }
    }

}
