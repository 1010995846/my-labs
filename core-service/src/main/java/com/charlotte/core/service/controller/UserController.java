package com.charlotte.core.service.controller;


import com.charlotte.core.service.entity.User;
import com.charlotte.core.service.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping(value = "/sys/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userSevice;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<User> userList(){
        List<User> userList = userSevice.findList();
        return userList;
    }
    
    @RequestMapping(value = "/register")
    @ResponseBody
    public void register(@RequestBody User user){
        userSevice.register(user);
    }

}
