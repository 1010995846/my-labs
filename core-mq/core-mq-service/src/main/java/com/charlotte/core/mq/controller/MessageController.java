package com.charlotte.core.mq.controller;

import com.charlotte.core.mq.dto.SysMessageDto;
import com.charlotte.core.mq.service.IMessageService;
import com.charlotte.core.mq.service.IMsgService;
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
    public void ack(SysMessageDto messageDto) {
        System.out.println("ack");
    }
}
