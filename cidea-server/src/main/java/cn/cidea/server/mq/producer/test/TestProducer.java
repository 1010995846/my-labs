package cn.cidea.server.mq.producer.test;

import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.server.mq.message.permission.ResourceRefreshMessage;
import cn.cidea.server.mq.message.test.TestRedisMessage;
import cn.cidea.server.mq.message.test.TestRedisRetryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TestProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    public void send() {
        log.info("[Redisson][发送 TEST 消息]");
        redisMQTemplate.send(new TestRedisMessage());
    }

    public void sendDelay() {
        log.info("[Redisson][Delay][发送 TEST 消息]");
        redisMQTemplate.sendDelay(new TestRedisMessage(), 10L, TimeUnit.SECONDS);
    }

    public void sendRetry() {
        log.info("[Redisson][Retry][发送 TEST 消息]");
        redisMQTemplate.send(new TestRedisRetryMessage());
    }

    public void pub() {
        log.info("[Redisson][pub][发送 TEST 消息]");
        redisMQTemplate.pub(new ResourceRefreshMessage());
    }
}
