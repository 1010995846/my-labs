package cn.cidea.framework.common.utils.mq.dataobject.entity.pool;


import cn.cidea.framework.common.utils.mq.dataobject.entity.SysMessage;
import cn.cidea.framework.common.utils.mq.service.IMessageService;
import cn.cidea.framework.common.utils.mq.service.IMqService;
import cn.cidea.framework.common.utils.mq.dataobject.dto.MessageDTO;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;

/**
 * @author Charlotte
 */
public class MsgBuilder {

    @Getter
    private SysMessage message;

    public MsgBuilder(SysMessage message) {
        this.message = message;
    }

    public void send(){
        IMqService mqService = SpringUtil.getBean(IMqService.class);
        mqService.sendMq(getDto());
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        message.setState("1");
        messageService.updateById(message);
    }

    public MessageDTO getDto(){
        return new MessageDTO().setMsgId(message.getMsgId()).setContext(message.getContext().getContext());
    }

}
