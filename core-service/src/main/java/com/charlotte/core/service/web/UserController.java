package com.charlotte.core.service.web;


import com.charlotte.core.service.entity.User;
import com.charlotte.core.service.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/sys")
@Slf4j
public class UserController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

//    @Autowired
    private IUserService userSevice;
//    @Qualifier("IUserService")
//    private IUserService userSeviceProxy;
//    @Autowired
//    private IInterface iInterface;
//    @Autowired
//    private JdkProxy jdkProxy;

    @RequestMapping(value = "/index")
    public Object index(){
        log.info("indo");
        log.debug("debugindo");
//        userSeviceProxy.findList();
        Object obj = null;
        obj = "index";
        obj = userSevice.findList();
        return obj;
    }

    @RequestMapping(value = "/userList")
    @ResponseBody
    public List<User> userList(){
        List<User> userList = userSevice.findList();
        return userList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
