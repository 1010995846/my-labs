package cn.cidea.module.admin.controller;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.admin.dataobject.convert.SysMessageConvert;
import cn.cidea.module.admin.dataobject.dto.SysMessageDTO;
import cn.cidea.module.admin.dataobject.entity.SysMessage;
import cn.cidea.module.admin.service.system.ISysMessageService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping("/sys/message")
public class MessageController {

    @Autowired
    private ISysMessageService messageService;

    @RequestMapping(value = "/resend")
    public Response resend(@RequestBody JSONObject param){
        messageService.resend(param.getLong("id"), param.getBoolean("retry"), param.getJSONObject("content"));
        return Response.success(null);
    }

    @RequestMapping(value = "/list")
    public Response resend(@RequestBody SysMessage query){
        List<SysMessage> list = messageService.list(new QueryWrapper<SysMessage>().lambda()
                .eq(query.getStatus() != null, SysMessage::getStatus, query.getStatus())
                .orderByDesc(SysMessage::getCreateTime));
        List<SysMessageDTO> dtoList = list.stream().map(SysMessageConvert.INSTANCE::toDTO).collect(Collectors.toList());
        return Response.success(dtoList);
    }

}