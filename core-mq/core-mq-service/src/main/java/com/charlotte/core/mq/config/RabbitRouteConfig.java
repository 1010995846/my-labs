package com.charlotte.core.mq.config;

import com.charlotte.core.mq.constants.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charlotte
 */
@Configuration
public class RabbitRouteConfig {

    @Bean(RabbitConstant.FANOUT_EXCHANGE_1)
    public Exchange fanoutExchange(){
        // fanout无规则，全推送
        return ExchangeBuilder.fanoutExchange(RabbitConstant.FANOUT_EXCHANGE_1).durable(true).build();
    }

    @Bean(RabbitConstant.TOPIC_EXCHANGE_1)
    public Exchange topicExchange(){
        // direct规则匹配routeKey
        // `.`分割区段；`*`匹配一个单词；`#`用于匹配0...n个单词
        return ExchangeBuilder.topicExchange(RabbitConstant.TOPIC_EXCHANGE_1).durable(true).build();
    }

    @Bean(RabbitConstant.DIRECT_EXCHANGE_1)
    public Exchange directExchange(){
        // direct完全匹配routeKey
        return ExchangeBuilder.directExchange(RabbitConstant.DIRECT_EXCHANGE_1).durable(true).build();
    }

    @Bean(RabbitConstant.HEADERS_EXCHANGE_1)
    public Exchange headersExchange(){
        // header匹配MessageProperties中的headers
        return ExchangeBuilder.headersExchange(RabbitConstant.HEADERS_EXCHANGE_1).durable(true).build();
    }

    @Bean(RabbitConstant.queue_1)
    public Queue queue1(){
        return QueueBuilder.durable(RabbitConstant.queue_1).build();
    }

    @Bean(RabbitConstant.queue_2)
    public Queue queue2(){
        return QueueBuilder.durable(RabbitConstant.queue_2).build();
    }

    /**
     * 指定 Exchange + routing key ，有且仅会路由到至多一个 Queue 中。未匹配则丢失
     */
    @Bean
    public Binding binding1(@Qualifier(RabbitConstant.TOPIC_EXCHANGE_1) Exchange exchange, @Qualifier(RabbitConstant.queue_1) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.route_1).noargs();
    }

    @Bean
    public Binding binding2(@Qualifier(RabbitConstant.TOPIC_EXCHANGE_1) Exchange exchange, @Qualifier(RabbitConstant.queue_2) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.route_1).noargs();
    }
}
