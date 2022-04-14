package cn.cidea.framework.mq.interceptor;

import cn.cidea.framework.mq.dto.AbstractMessage;

/**
 * MQ拦截器，作为插件机制，实现拓展。
 * @author Charlotte
 */
public interface MessageInterceptor {

    default void sendMessageBefore(AbstractMessage message) {
    }

    default void sendMessageAfter(AbstractMessage message) {
    }

    default void consumeMessageBefore(AbstractMessage message) {
    }

    default void consumeMessageAfter(AbstractMessage message) {
    }

}
