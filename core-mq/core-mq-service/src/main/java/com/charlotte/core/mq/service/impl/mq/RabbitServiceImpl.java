package com.charlotte.core.mq.service.impl.mq;

import com.alibaba.fastjson.JSONObject;
import com.charlotte.core.mq.service.IMqService;
import com.charlotte.core.mq.constants.RabbitConstant;
import com.charlotte.core.mq.dto.SysMessageDto;
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
    public void sendMq(SysMessageDto messageDto) {
        rabbitTemplate.convertAndSend(RabbitConstant.TOPIC_EXCHANGE_1, RabbitConstant.route_1, messageDto);
        log.info("rabbit发送完毕");
    }
}
