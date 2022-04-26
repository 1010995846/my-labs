package cn.cidea.framework.mq.redis.interceptor;

import cn.cidea.framework.mq.redis.core.RedisMQTemplate;
import cn.cidea.framework.mq.redis.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redis.core.message.AbstractMessage;
import cn.cidea.framework.mq.redis.core.message.MessageIdGenerator;
import cn.cidea.framework.mq.redis.properties.RedisMqProperties;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlotte
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CideaInterceptor implements MessageInterceptor {

    @Autowired
    private RedisMQTemplate mqTemplate;
    @Autowired
    private RedisMqProperties properties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired(required = false)
    private MessageIdGenerator idGenerator;

    @Override
    public void sendBefore(AbstractMessage message) {
        long id;
        if(idGenerator != null){
            id = idGenerator.nextId();
        } else {
            RIdGenerator idGenerator = redissonClient.getIdGenerator("TT");
            id = idGenerator.nextId();
        }
        message.setId(id);
        log.debug("[消息拦截器][发送消息]前置处理, id = {}", message.getId());
    }

    @Override
    public void sendAfter(AbstractMessage message) {
        log.debug("[消息拦截器][发送消息]后置处理, id = {}", message.getId());
    }

    @Override
    public void consumeBefore(AbstractMessage message) {
        log.debug("[消息拦截器][消费消息]前置处理, id = {}", message.getId());
    }

    @Override
    public void consumeSuccess(AbstractMessage message) {
        log.debug("[消息拦截器][消费消息]消费成功, id = {}", message.getId());
    }

    @Override
    public void consumeError(AbstractMessage message, RuntimeException e) {
        log.error("[消息拦截器][消费消息]消费异常, message = " + JSONObject.toJSONString(message), e);
        if(!message.retry()){
            log.info("消费消息参数不进行重试");
            return;
        }
        Duration[] retryInterval = properties.getRetryInterval();
        if(retryInterval == null || retryInterval.length == 0){
            log.info("未设定重试间隔，不进行重试");
            return;
        }

        Integer retry = message.nextRetry();
        if(retry <= retryInterval.length){
            Duration duration = retryInterval[retry - 1];
            log.info("第{}次重试，下次重试: {} milliseconds", retry, duration.toMillis());
            mqTemplate.sendDelay(message, duration.toMillis(), TimeUnit.MILLISECONDS);
        } else {
            log.warn("重试超出次数");
            // TODO 是否要参考死信队列留存一份
        }
    }

    @Override
    public void consumeFinally(AbstractMessage message) {
        log.debug("[消息拦截器][消费消息]后置处理, id = {}", message.getId());
    }

}
