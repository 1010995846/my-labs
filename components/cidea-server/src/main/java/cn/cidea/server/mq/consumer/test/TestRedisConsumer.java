package cn.cidea.server.mq.consumer.test;

import cn.cidea.framework.mq.redis.core.message.AbstractMessageListener;
import cn.cidea.server.mq.message.test.TestRedisMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestRedisConsumer extends AbstractMessageListener<TestRedisMessage> {

    @Override
    public void consume(TestRedisMessage message) {
        log.warn("[Redisson][onMessage][收到 TEST 消息]");
    }

}