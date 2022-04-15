package cn.cidea.server.controller;


import cn.cidea.framework.web.core.api.Response;
import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.mq.producer.permission.ResourceProducer;
import cn.cidea.server.service.system.ISysResourceService;
import cn.cidea.server.service.system.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping(value = "")
@Slf4j
public class TestController {

    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysResourceService resourceService;
    @Autowired
    private ResourceProducer resourceProducer;

    @RequestMapping(value = "/test")
    // 此处设置无用，Filter中已经设置了大部分访问需要登录，两者叠加
    @PreAuthorize("permitAll()")
    public Response login(@RequestBody JSONObject param){
        resourceProducer.sendRefreshMessage();
        return Response.success(null);
    }

}
