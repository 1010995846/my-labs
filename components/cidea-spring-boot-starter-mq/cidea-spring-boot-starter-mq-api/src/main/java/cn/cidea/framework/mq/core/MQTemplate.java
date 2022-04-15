package cn.cidea.framework.mq.core;

import cn.cidea.framework.mq.core.dto.AbstractMessage;
import cn.cidea.framework.mq.core.interceptor.MessageInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Charlotte
 */
public abstract class MQTemplate<T extends AbstractMessage> {

    protected abstract void doSend(T message);

    /**
     * 拦截器数组
     */
    protected final List<MessageInterceptor> interceptors = new ArrayList<>();

    /**
     * 发送 Redis 消息，基于 Redis pub/sub 实现
     *
     * @param message 消息
     */
    public void send(T message) {
        try {
            sendMessageBefore(message);
            // 发送消息
            doSend(message);
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
