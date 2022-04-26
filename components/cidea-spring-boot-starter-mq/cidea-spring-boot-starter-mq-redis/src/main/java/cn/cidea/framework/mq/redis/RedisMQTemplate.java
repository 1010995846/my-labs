package cn.cidea.framework.mq.redis;

import cn.cidea.framework.mq.redis.core.MessageInterceptor;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 消息模板
 * @author Charlotte
 */
public class RedisMQTemplate<T extends AbstractMessage> {

    @Autowired
    @Getter
    private StringRedisTemplate redisTemplate;

    /**
     * 拦截器
     */
    @Autowired
    protected final List<MessageInterceptor> interceptors = new ArrayList<>();

    protected void doPub(T message) {
        // 发送消息
        redisTemplate.convertAndSend(message.getChannel(), JSONObject.toJSONString(message));
    }

    protected void doSend(T message) {

    }

    protected void doSendDelay(T message, long delay, TimeUnit timeUnit){

    }

    /**
     * 发送 Redis 消息，基于 Redis pub/sub 实现
     *
     * @param message 消息
     */
    public void pub(T message) {
        around(message, this::doPub);
    }

    public void send(T message) {
        around(message, this::doSend);
    }

    public void sendDelay(T message, Long timeout, TimeUnit timeUnit) {
        around(message, msg -> doSendDelay(msg, timeout, timeUnit));
    }

    public void around(T message, Consumer<T> consumer){
        try {
            sendMessageBefore(message);
            // 发送消息
            consumer.accept(message);
        } catch (RuntimeException e) {
            sendMessageAfterError(message, e);
            throw e;
        } finally {
            sendMessageAfter(message);
        }
    }

    protected void sendMessageBefore(T message) {
        // 正序
        interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
    }

    protected void sendMessageAfter(T message) {
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).sendMessageAfter(message);
        }
    }

    protected void sendMessageAfterError(T message, RuntimeException e) {
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).sendMessageAfter(message);
        }
    }

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     */
    public void addInterceptor(MessageInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public List<MessageInterceptor> getInterceptors() {
        return interceptors;
    }

}
