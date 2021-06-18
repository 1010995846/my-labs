package com.charlotte.core.mq.service.impl;

import com.charlotte.core.mq.service.IMqConsumer;
import com.charlotte.core.mq.constants.RabbitConstant;
import com.charlotte.core.mq.dto.SysMessageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
public class RabbitConsumerImpl implements IMqConsumer {

    @RabbitListener(queues = RabbitConstant.queue_1)
    public void recive1(SysMessageDto message){
        System.out.println(message);
        return;
    }

    @RabbitListener(queues = RabbitConstant.queue_2)
    public void recive2(Message message){
        System.out.println(new String(message.getBody()));
        return;
    }

}
