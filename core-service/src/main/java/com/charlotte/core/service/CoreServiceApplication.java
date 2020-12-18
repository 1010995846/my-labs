package com.charlotte.core.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CoreServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CoreServiceApplication.class, args);
        Object obj;
        obj = context.getBean("");
        obj = context.getBean("userService");
        obj = context.getBean("u1");
        obj = context.getBean("u2");
        return;
    }

}
