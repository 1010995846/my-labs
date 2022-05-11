package cn.cidea.framework.mq.redis.plugin.interceptor;

import cn.cidea.framework.mq.redis.core.RedisMQTemplate;
import cn.cidea.framework.mq.redis.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redis.core.message.AbstractMessage;
import cn.cidea.framework.mq.redis.properties.RedisMqProperties;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlotte
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class MessageRetryInterceptor implements MessageInterceptor {

    @Autowired
    private RedisMQTemplate mqTemplate;
    @Autowired
    private RedisMqProperties properties;

    @Override
    public void consumeError(AbstractMessage message, RuntimeException e) {
        log.error("[消息拦截器][消费消息]消费异常, message = " + JSONObject.toJSONString(message), e);
        if (!needRetry(message)) {
            log.info("消费消息参数不进行重试");
            message.setNextDuration(null);
            return;
        }
        Duration[] retryInterval = properties.getRetryInterval();
        if (retryInterval == null || retryInterval.length == 0) {
            log.info("未设定重试间隔，不进行重试");
            message.setNextDuration(null);
            return;
        }

        Integer retry = message.getRetry();
        if (retry == null) {
            retry = 0;
            message.setRetry(0);
        }
        if (retry < retryInterval.length) {
            Duration duration = retryInterval[retry];
            message.setNextDuration(duration);
        } else {
            log.warn("重试超出次数");
            message.setNextDuration(null);
            // TODO 是否要参考死信队列留存一份
        }
    }


    @Override
    public void consumeFinally(AbstractMessage message) {
        Duration nextDuration = message.getNextDuration();
        if (nextDuration == null) {
            return;
        }
        Integer nextRetry = nextRetry(message);
        message.setNextDuration(null);
        log.info("第{}次重试，重试间隔: {} milliseconds", nextRetry, nextDuration.toMillis());
        mqTemplate.sendDelay(message, nextDuration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 是否进行重试
     * 小于0时不进行自动重试
     *
     * @return
     */
    private boolean needRetry(AbstractMessage message) {
        return message.getRetry() == null || message.getRetry() >= 0;
    }

    /**
     * 获取下一次的重试次数
     *
     * @return
     */
    private int nextRetry(AbstractMessage message) {
        if (message.getRetry() == null) {
            message.setRetry(1);
        } else {
            message.setRetry(message.getRetry() + 1);
        }
        return message.getRetry();
    }

}
