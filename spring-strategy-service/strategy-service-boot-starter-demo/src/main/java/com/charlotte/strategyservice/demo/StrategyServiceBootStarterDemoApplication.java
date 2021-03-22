package com.charlotte.strategyservice.demo;

import com.charlotte.strategyservice.demo.facade.OrgFacade;
import com.charlotte.strategyservice.demo.service.IOrgService;
import com.charlotte.strategyservice.demo.service.impl.HospService;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Charlotte
 */
@SpringBootApplication
public class StrategyServiceBootStarterDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StrategyServiceBootStarterDemoApplication.class, args);

        /**
         * 调用接口实例
         */
        IOrgService implBean = context.getBean(IOrgService.class);
        System.out.println("name = " + implBean.getName());
        System.out.println("name = " + implBean.getName());
        System.out.println("name = " + implBean.getName());
        System.out.println("name = " + implBean.getName());
        System.out.println("name = " + implBean.getName());

        /**
         * 调用无接口实例，需要main为branch的父类之一
         */
//        Object rootFacade = context.getBean("rootFacade");
//        System.out.println();
        // TODO Charlotte: 2020/12/24 当未实现接口时若同时存在spring注解注入的分支，context.getBean(...)的所有重载方法均获取不到主类，疑似与被代理，getClass()不同有关
        Object orgFacade = context.getBean("orgFacade");
        OrgFacade noImplBean = context.getBean(OrgFacade.class);
        noImplBean.print();
        noImplBean.print();
        noImplBean.print();
        noImplBean.print();
        noImplBean.print();
    }

}
