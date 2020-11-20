package com.charlotte.strategyservice.demo;

import com.charlotte.strategyservice.demo.facade.OrgFacade;
import com.charlotte.strategyservice.demo.service.IOrgService;
import com.charlotte.strategyservice.demo.service.impl.HospService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StrategyServiceBootStarterDemoApplication {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ConfigurableApplicationContext context = SpringApplication.run(StrategyServiceBootStarterDemoApplication.class, args);

        IOrgService bean = context.getBean(IOrgService.class);
        System.out.println(bean.getName());
        System.out.println(bean.getName());
        System.out.println(bean.getName());
        System.out.println(bean.getName());
        System.out.println(bean.getName());

        OrgFacade facadeBean = context.getBean(OrgFacade.class);
        facadeBean.print();
        facadeBean.print();
        facadeBean.print();
        facadeBean.print();
        facadeBean.print();
    }

}
