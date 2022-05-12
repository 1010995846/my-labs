package cn.cidea.framework.mq.redisson.core.message;

import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.framework.mq.redisson.core.interceptor.MessageInterceptor;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.SneakyThrows;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 监听基类
 * @author Charlotte
 * @param <T> 监听的消息类型
 */
public abstract class AbstractMessageListener<T extends AbstractMessage> implements MessageListener<String> {

    /**
     * 消息类型
     */
    @Getter
    protected final Class<T> messageType;
    /**
     * Redis Channel
     */
    @Getter
    protected final String channelName;
    /**
     * RedisMQTemplate
     */
    @Autowired
    protected RedisMQTemplate mqTemplate;

    @SneakyThrows
    protected AbstractMessageListener() {
        this.messageType = getMessageClass();
        this.channelName = messageType.newInstance().getChannel().toString();
    }

    @Override
    public void onMessage(CharSequence channel, String msg) {
        if(!channel.equals(this.getChannelName())){
            return;
        }
        T messageObj = JSONObject.parseObject(msg, messageType);
        onMessage(messageObj);
    }

    private void onMessage(T messageObj) {
        try {
            consumeMessageBefore(messageObj);
            // 消费消息
            this.consume(messageObj);
            consumeMessageSuccess(messageObj);
        } catch (RuntimeException e){
            consumeMessageError(messageObj, e);
            throw e;
        } finally {
            consumeMessageFinally(messageObj);
        }
    }

    /**
     * 处理消息
     *
     * @param message 消息
     */
    public abstract void consume(T message);

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

    private void consumeMessageBefore(T message) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 正序
        interceptors.forEach(interceptor -> interceptor.consumeBefore(message));
    }

    private void consumeMessageFinally(T message) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).consumeFinally(message);
        }
    }

    private void consumeMessageSuccess(T message) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).consumeSuccess(message);
        }
    }

    private void consumeMessageError(T message, RuntimeException e) {
        assert mqTemplate != null;
        List<MessageInterceptor> interceptors = mqTemplate.getInterceptors();
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).consumeError(message, e);
        }
    }

}
