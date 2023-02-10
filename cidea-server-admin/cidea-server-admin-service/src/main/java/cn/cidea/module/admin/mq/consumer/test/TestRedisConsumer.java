package cn.cidea.module.admin.mq.consumer.test;

import cn.cidea.framework.mq.redisson.core.message.AbstractMessageListener;
import cn.cidea.module.admin.mq.message.test.TestRedisMessage;
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
