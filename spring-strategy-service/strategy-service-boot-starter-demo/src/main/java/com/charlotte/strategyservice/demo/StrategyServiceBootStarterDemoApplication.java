package com.charlotte.strategyservice.demo;

import com.charlotte.strategyservice.demo.facade.OrgFacade;
import com.charlotte.strategyservice.demo.service.IOrgService;
import com.charlotte.strategyservice.demo.service.impl.HospService;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StrategyServiceBootStarterDemoApplication {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ConfigurableApplicationContext context = SpringApplication.run(StrategyServiceBootStarterDemoApplication.class, args);

        /**
         * 调用接口实例
         */
        IOrgService implBean = context.getBean(IOrgService.class);
        System.out.println(implBean.getName());
        System.out.println(implBean.getName());
        System.out.println(implBean.getName());
        System.out.println(implBean.getName());
        System.out.println(implBean.getName());

        /**
         * 调用无接口实例，需要main为branch的父类之一
         */
//        OrgFacade noImplBean = context.getBean(OrgFacade.class);
//        noImplBean.print();
//        noImplBean.print();
//        noImplBean.print();
//        noImplBean.print();
//        noImplBean.print();
    }

}
