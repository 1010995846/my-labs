package com.charlotte.core.mq.pool;


import com.charlotte.core.mq.service.IMqService;
import com.charlotte.core.service.utils.SpringContextUtils;
import com.charlotte.core.mq.entity.SysMessage;
import com.charlotte.core.mq.service.IMessageService;
import com.charlotte.core.mq.dto.SysMessageDto;
import lombok.Getter;

/**
 * @author Charlotte
 */
public class MsgPool {

    @Getter
    private SysMessage message;

    public MsgPool(SysMessage message) {
        this.message = message;
    }

    public void send(){
        IMqService mqService = SpringContextUtils.getApplicationContext().getBean(IMqService.class);
        mqService.sendMq(getDto());
        IMessageService messageService = SpringContextUtils.getApplicationContext().getBean(IMessageService.class);
        message.setState("1");
        messageService.updateById(message);
    }

    public SysMessageDto getDto(){
        return new SysMessageDto().setMsgId(message.getMsgId()).setContext(message.getContext().getContext());
    }

}
