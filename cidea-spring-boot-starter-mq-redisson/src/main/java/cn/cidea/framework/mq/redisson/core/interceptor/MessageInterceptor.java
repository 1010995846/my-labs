package cn.cidea.framework.mq.redisson.core.interceptor;


import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;

/**
 * MQ拦截器，作为插件机制，实现拓展。
 * @author Charlotte
 */
public interface MessageInterceptor {

    default void sendBefore(AbstractMessage message) {
    }

    default void sendAfter(AbstractMessage message) {
    }

    default void consumeBefore(AbstractMessage message) {
    }

    default void consumeFinally(AbstractMessage message) {
    }

    default void consumeSuccess(AbstractMessage message) {
    }

    default void consumeError(AbstractMessage message, RuntimeException e) {
    }

}
