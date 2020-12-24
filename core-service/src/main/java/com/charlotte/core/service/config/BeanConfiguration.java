package com.charlotte.core.service.config;

import com.charlotte.core.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean(name = {"u1", "u2"})
    public IUserService userService(@Autowired IUserService userService){
        return userService;
    }


}
