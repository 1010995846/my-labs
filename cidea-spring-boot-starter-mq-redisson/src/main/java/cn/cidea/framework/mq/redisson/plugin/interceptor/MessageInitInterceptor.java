package cn.cidea.framework.mq.redisson.plugin.interceptor;

import cn.cidea.framework.mq.redisson.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import cn.cidea.framework.mq.redisson.core.message.MessageIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Date;

/**
 * @author Charlotte
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MessageInitInterceptor implements MessageInterceptor {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired(required = false)
    private MessageIdGenerator idGenerator;

    @Override
    public void sendBefore(AbstractMessage message) {
        if(message.getId() == null) {
            Long id;
            if (idGenerator != null) {
                id = idGenerator.nextId();
            } else {
                RIdGenerator idGenerator = redissonClient.getIdGenerator("REDIS_MQ");
                id = idGenerator.nextId();
            }
            message.setId(id);
        }
        message.setSendTime(new Date());
        log.debug("[消息拦截器][发送消息]前置处理, id = {}", message.getId());
    }

}
