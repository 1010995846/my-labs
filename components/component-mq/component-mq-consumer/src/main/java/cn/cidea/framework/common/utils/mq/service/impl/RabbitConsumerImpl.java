package cn.cidea.framework.common.utils.mq.service.impl;

import cn.cidea.framework.common.utils.mq.service.IMqConsumer;
import cn.cidea.framework.common.utils.mq.constants.RabbitConstant;
import cn.cidea.framework.common.utils.mq.dto.SysMessageDto;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Charlotte
 */
@Service
public class RabbitConsumerImpl implements IMqConsumer {

//    @RabbitListener(queues = RabbitConstant.queue_1)
//    public void recive1(SysMessageDto message){
//        System.out.println(message);
//        return;
//    }
    @RabbitListener(queues = RabbitConstant.queue_1)
    public void recive1(SysMessageDto dto, Message message, Channel channel) throws IOException, TimeoutException {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.out.println(dto);
        // multiple 表示是否批量处理。true表示批量ack处理小于tag的所有消息。false则处理当前消息
        // ack
        channel.basicAck(tag, false);
        // Nack，拒绝策略，消息重回队列。代表可重试
        channel.basicNack(tag, false, true);
        // Nack，拒绝策略，并且从队列中删除
        channel.basicNack(tag, false, false);
        channel.close();
        return;
    }

    @RabbitListener(queues = RabbitConstant.queue_2)
    public void recive2(Message message){
        System.out.println(new String(message.getBody()));
        return;
    }

}
