package cn.cidea.framework.mq.redis.core.message.pubsub;

import cn.cidea.framework.mq.redis.AbstractMessage;
import cn.cidea.framework.mq.redis.RedisMQTemplate;
import cn.cidea.framework.mq.redis.core.MessageInterceptor;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 监听基类
 * @author Charlotte
 * @param <T> 监听的消息类型
 */
public abstract class AbstractChannelMessageListener<T extends AbstractMessage> implements MessageListener {

    /**
     * 消息类型
     */
    @Getter
    protected final Class<T> messageType;
    /**
     * Redis Channel
     */
    protected final String channel;
    /**
     * RedisMQTemplate
     */
    @Autowired
    protected RedisMQTemplate mqTemplate;

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
    public void onMessage(Message message, byte[] pattern) {
        T messageObj = JSONObject.parseObject(message.getBody(), messageType);
        onMessage(messageObj);
    }

    private void onMessage(T messageObj) {
        try {
            consumeMessageBefore(messageObj);
            // 消费消息
            this.invoke(messageObj);
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
    public abstract void invoke(T message);

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
