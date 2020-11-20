package com.charlotte.lab.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String BUSINESS_EXCHANGE = "simple.business.exchange";

    public static final String BUSINESS_QUEUE_A = "simple.business.queue.a";

    public static final String BUSINESS_QUEUE_B = "simple.business.queue.b";
    // 死信队列交换机
    public static final String DEAD_LETTER_EXCHANGE = "simple.deadletter.exchange";
    // 死信队列A路由
    public static final String DEAD_LETTER_QUEUE_A_ROUTING_KEY = "simple.deadletter.queue.a.routingkey";
    // 死信队列B路由
    public static final String DEAD_LETTER_QUEUE_B_ROUTING_KEY = "simple.deadletter.queue.b.routingkey";
    // 死信队列A
    public static final String DEAD_LETTER_QUEUE_A = "simple.deadletter.queue.a";
    // 死信队列B
    public static final String DEAD_LETTER_QUEUE_B = "simple.deadletter.queue.b";

    // 声明业务Exchange
    @Bean("businessExchange")
    public FanoutExchange businessExchange() {
        return new FanoutExchange(BUSINESS_EXCHANGE);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 声明业务队列A
    @Bean("businessQueueA")
    public Queue businessQueueA() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_A_ROUTING_KEY);
        return QueueBuilder.durable(BUSINESS_QUEUE_A).withArguments(args).build();
    }

    // 声明业务队列B
    @Bean("businessQueueB")
    public Queue businessQueueB() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_B_ROUTING_KEY);
        return QueueBuilder.durable(BUSINESS_QUEUE_B).withArguments(args).build();
    }

    // 声明死信队列A
    @Bean("deadLetterQueueA")
    public Queue deadLetterQueueA() {
        return new Queue(DEAD_LETTER_QUEUE_A);
    }

    // 声明死信队列B
    @Bean("deadLetterQueueB")
    public Queue deadLetterQueueB() {
        return new Queue(DEAD_LETTER_QUEUE_B);
    }

    // 声明业务队列A绑定业务交换机
    @Bean
    public Binding businessBindingA(@Qualifier("businessQueueA") Queue queue,
                                    @Qualifier("businessExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 声明业务队列B绑定业务交换机
    @Bean
    public Binding businessBindingB(@Qualifier("businessQueueB") Queue queue,
                                    @Qualifier("businessExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 声明死信队列A绑定死信交换机
    @Bean
    public Binding deadLetterBindingA(@Qualifier("deadLetterQueueA") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUE_A_ROUTING_KEY);
    }

    // 声明死信队列B绑定死信交换机
    @Bean
    public Binding deadLetterBindingB(@Qualifier("deadLetterQueueB") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUE_B_ROUTING_KEY);
    }
}
