package cn.cidea.lab.mq.web;

import cn.cidea.lab.mq.service.BusinessMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rabbitmq")
@RestController
public class RabbitController {

    @Autowired
    private BusinessMessageSender sender;

    @PostMapping("/send")
    public void sendMsg(@RequestParam String routingKey, @RequestParam String msg) {
        sender.send(routingKey, msg);
    }

}
