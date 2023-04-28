package cn.cidea.framework.common.utils.mq.controller;

import cn.cidea.framework.common.utils.mq.dataobject.dto.MessageDTO;
import cn.cidea.framework.common.utils.mq.dataobject.dto.SysMessageDTO;
import cn.cidea.framework.common.utils.mq.dataobject.entity.SysMessage;
import cn.cidea.framework.common.utils.mq.service.IMessageService;
import cn.cidea.framework.common.utils.mq.service.IMsgService;
import cn.cidea.framework.common.utils.mq.service.ISysMessageService;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.framework.common.utils.mq.dataobject.convert.SysMessageConvert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping("/sys/msg")
public class MessageController implements IMsgService {

    @Autowired
    private IMessageService messageService;
    @Autowired
    private ISysMessageService sysMessageService;

    @PostMapping("/send")
    public void send(@RequestBody String context){
        messageService.send(context);
    }

    @PostMapping("/ack")
    @Override
    public void ack(MessageDTO messageDto) {
        System.out.println("ack");
    }


    @RequestMapping(value = "/resend")
    public Response resend(@RequestBody JSONObject param){
        sysMessageService.resend(param.getLong("id"), param.getBoolean("retry"), param.getJSONObject("content"));
        return Response.success(null);
    }

    @RequestMapping(value = "/list")
    public Response list(@RequestBody SysMessage query){
        List<SysMessage> list = sysMessageService.list(new QueryWrapper<SysMessage>().lambda()
                .eq(query.getStatus() != null, SysMessage::getStatus, query.getStatus())
                .orderByDesc(SysMessage::getCreateTime));
        List<SysMessageDTO> dtoList = list.stream().map(SysMessageConvert.INSTANCE::toDTO).collect(Collectors.toList());
        return Response.success(dtoList);
    }

}
