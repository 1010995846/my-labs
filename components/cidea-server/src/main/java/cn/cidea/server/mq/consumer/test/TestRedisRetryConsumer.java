package cn.cidea.server.mq.consumer.test;

import cn.cidea.framework.mq.redis.core.message.AbstractMessageListener;
import cn.cidea.server.mq.message.test.TestRedisMessage;
import cn.cidea.server.mq.message.test.TestRedisRetryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestRedisRetryConsumer extends AbstractMessageListener<TestRedisRetryMessage> {

    @Override
    public void consume(TestRedisRetryMessage message) {
        log.warn("[Redisson][onMessage][收到 TEST 消息]");
        if(1 == 1){
            throw new RuntimeException("模拟异常，进行消息重试");
        }
    }

}