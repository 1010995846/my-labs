package cn.cidea.module.admin.mq.consumer.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Component
@Slf4j
public class TestRabbitConsumer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test_queue", durable = "true"),
            exchange = @Exchange(value = "cidea.delayed", delayed = "true"),
            key = "test_queue")
    )
    @RabbitHandler
    public void invoke(String message) {
        log.warn("[Rabbit][onMessage][收到 TEST 消息]");
    }

}
