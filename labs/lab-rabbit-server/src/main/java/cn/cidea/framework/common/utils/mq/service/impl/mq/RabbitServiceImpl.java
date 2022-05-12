package cn.cidea.framework.common.utils.mq.service.impl.mq;

import cn.cidea.framework.common.utils.mq.service.IMqService;
import cn.cidea.framework.common.utils.mq.constants.RabbitConstant;
import cn.cidea.framework.common.utils.mq.dto.SysMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Charlotte
 */
@Primary
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "host")
public class RabbitServiceImpl implements IMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(readOnly = true)
    public void sendMq(SysMessageDTO messageDto) {
//        spring:
//        rabbitmq:
//        publisher-confirms: true
//        publisher-returns: true
//        template:
//        mandatory: true
        // 发送失败，ack
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息投递成功~消息Id：{}", correlationData.getId());
            } else {
                log.error("消息投递失败，Id：{}，错误提示：{}", correlationData.getId(), cause);
            }
        });


        // 路由失败：没有被队列接收；匹配到队列但失败
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned -> {
            log.info("消息没有路由到队列，获得返回的消息");
            log.info("message body: {}", returned.getMessage());
            log.info("replyCode: {}", returned.getReplyCode());
            log.info("replyText: {}", returned.getReplyText());
            log.info("exchange: {}", returned.getExchange());
            log.info("routingKey: {}", returned.getRoutingKey());
        });

//        rabbitTemplate.convertAndSend(RabbitConstant.TOPIC_EXCHANGE_1, RabbitConstant.route_1, messageDto);
        rabbitTemplate.convertAndSend(RabbitConstant.TOPIC_EXCHANGE_1, RabbitConstant.route_1, messageDto);
        log.info("rabbit发送完毕");
    }
}
