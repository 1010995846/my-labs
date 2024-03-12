package cn.cidea.module.admin.controller;


import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.admin.mq.producer.permission.ResourceProducer;
import cn.cidea.module.admin.mq.producer.test.TestProducer;
import cn.cidea.module.admin.service.ISysResourceService;
import cn.cidea.module.admin.service.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Charlotte
 */
@Slf4j
@RestController
@RequestMapping(value = "")
public class TestController {

    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysResourceService resourceService;
    @Autowired
    private ResourceProducer resourceProducer;
    @Autowired
    private TestProducer testProducer;

    @RequestMapping(value = "/test/1")
    // 此处设置无用，Filter中已经设置了大部分访问需要登录，两者叠加
    @PreAuthorize("permitAll()")
    public Response test1(@RequestBody JSONObject param){
        // testProducer.send();
        // testProducer.sendDelay();
        testProducer.sendRetry();
        // testProducer.pub();
        return Response.success(null);
    }

    @RequestMapping(value = "/test/2")
    // 此处设置无用，Filter中已经设置了大部分访问需要登录，两者叠加
    @PreAuthorize("permitAll()")
    public Response test2(@RequestBody JSONObject param){
        return Response.success(null);
    }

    @RequestMapping(value = "/test/3")
    @PreAuthorize("permitAll()")
    public Response test3(@RequestBody JSONObject param){
        roleService.delete(4895L);
        return Response.success(null);
    }
}
