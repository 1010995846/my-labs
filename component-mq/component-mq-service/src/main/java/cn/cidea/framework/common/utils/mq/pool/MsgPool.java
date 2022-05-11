package cn.cidea.framework.common.utils.mq.pool;


import cn.cidea.framework.common.utils.mq.entity.SysMessage;
import cn.cidea.framework.common.utils.mq.service.IMessageService;
import cn.cidea.framework.common.utils.mq.service.IMqService;
import cn.cidea.framework.common.utils.mq.utils.SpringContextUtils;
import cn.cidea.framework.common.utils.mq.dto.SysMessageDto;
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
