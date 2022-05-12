package cn.cidea.framework.mq.redisson.core;

import cn.cidea.framework.mq.redisson.config.RedissonMqAutoConfiguration;
import cn.cidea.framework.mq.redisson.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redis MQ 操作模板类
 * @author Charlotte
 */
public class RedisMQTemplate {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 拦截器
     */
    @Getter
    @Autowired(required = false)
    protected List<MessageInterceptor> interceptors = new ArrayList<>();

    /**
     * pub
     * 监听器sub {@link RedissonMqAutoConfiguration.PubSubAutoConfiguration#PubSubAutoConfiguration(RedissonClient, List)}
     * @param message
     */
    protected <T extends AbstractMessage> void doPub(T message) {
        RTopic topic = redissonClient.getTopic(message.getChannel().toString());
        // TODO 直接使用对象会有类加载不一致问题，原因不明，暂时使用JSONString
        topic.publish(JSONObject.toJSONString(message));
        // topic.publish(message);
    }

    protected <T extends AbstractMessage> void doSend(T message) {
        RQueue<String> queue = redissonClient.getQueue(message.getChannel().toString());
        queue.add(JSONObject.toJSONString(message));
    }

    protected <T extends AbstractMessage> void doSendDelay(T message, long delay, TimeUnit timeUnit) {
        RQueue<String> queue = redissonClient.getQueue(message.getChannel().toString());
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(queue);
        delayedQueue.offer(JSONObject.toJSONString(message), delay, timeUnit);
    }

    /**
     * 发送 Redis 消息，基于 Redis pub/sub 实现
     *
     * @param message 消息
     */
    public <T extends AbstractMessage> void pub(T message) {
        around(message, this::doPub);
    }

    public <T extends AbstractMessage> void send(T message) {
        around(message, this::doSend);
    }

    public <T extends AbstractMessage> void sendDelay(T message, Long timeout, TimeUnit timeUnit) {
        around(message, msg -> doSendDelay(msg, timeout, timeUnit));
    }

    private  <T extends AbstractMessage> void around(T message, Consumer<T> consumer){
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

    protected <T extends AbstractMessage> void sendMessageBefore(T message) {
        // 正序
        interceptors.forEach(interceptor -> interceptor.sendBefore(message));
    }

    protected <T extends AbstractMessage> void sendMessageAfter(T message) {
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).sendAfter(message);
        }
    }

    protected <T extends AbstractMessage> void sendMessageAfterError(T message, RuntimeException e) {
        // 倒序
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).sendAfter(message);
        }
    }
}
