package cn.cidea.server.mq.consumer.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Component
@RabbitListener(queues = "test_queue")
@Slf4j
public class TestRabbitConsumer {

    @RabbitHandler
    public void invoke(String message) {
        log.warn("[Rabbit][onMessage][收到 TEST 消息]");
    }

}
