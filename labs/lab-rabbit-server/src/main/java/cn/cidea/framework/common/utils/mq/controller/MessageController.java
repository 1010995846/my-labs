package cn.cidea.framework.common.utils.mq.controller;

import cn.cidea.framework.common.utils.mq.dto.SysMessageDTO;
import cn.cidea.framework.common.utils.mq.service.IMessageService;
import cn.cidea.framework.common.utils.mq.service.IMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping("/sys/msg")
public class MessageController implements IMsgService {

    @Autowired
    private IMessageService mqService;

    @PostMapping("/send")
    public void send(@RequestBody String context){
        mqService.send(context);
    }

    @PostMapping("/ack")
    @Override
    public void ack(SysMessageDTO messageDto) {
        System.out.println("ack");
    }
}
