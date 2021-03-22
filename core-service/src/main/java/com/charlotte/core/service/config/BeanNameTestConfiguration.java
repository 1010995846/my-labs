package com.charlotte.core.service.config;

import com.charlotte.core.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charlotte
 */
@Deprecated
//@Configuration
public class BeanNameTestConfiguration {

    // 只有name的第一个值会作为beanName，后面都是alias
    @Bean(name = {"u1", "u2"})
    public IUserService userService(@Autowired IUserService userService){
        return userService;
    }


}
