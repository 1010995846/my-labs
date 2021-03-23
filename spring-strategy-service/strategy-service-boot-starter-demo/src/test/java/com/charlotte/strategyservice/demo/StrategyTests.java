package com.charlotte.strategyservice.demo;

import com.charlotte.strategyservice.demo.config.OrgStrategyProxy;
import com.charlotte.strategyservice.demo.facade.OrgFacade;
import com.charlotte.strategyservice.demo.service.IGunService;
import com.charlotte.strategyservice.demo.service.IOrgService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StrategyServiceBootStarterDemoApplication.class)
class StrategyTests implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Test
    void normal() {
        System.out.println("期望：轮询");
        IGunService implBean = beanFactory.getBean(IGunService.class);
        for (int i = 0; i < 7; i++) {
            implBean.shoot();
        }
    }

    @Test
    void route() {
        System.out.println("期望：路由");
        IOrgService implBean = beanFactory.getBean(IOrgService.class);
        OrgStrategyProxy.routeKey.set("school");
        System.out.println("name = " + implBean.getName());// 打印school
        OrgStrategyProxy.routeKey.set("hosp");
        System.out.println("name = " + implBean.getName());// 打印hosp
        System.out.println("name = " + implBean.getName());// 打印hosp
        OrgStrategyProxy.routeKey.set("ext");
        System.out.println("name = " + implBean.getName());// 打印ext
        OrgStrategyProxy.routeKey.remove();
        System.out.println("name = " + implBean.getName());// 打印defaultOrg
    }

    @Test
    void multipleProxy(){
        // 代理一，指定路由
        System.out.println("期望：路由");
        IOrgService implBean = beanFactory.getBean(IOrgService.class);
        OrgStrategyProxy.routeKey.set("school");
        System.out.println("name = " + implBean.getName());// 打印school
        System.out.println("name = " + implBean.getName());// 打印school

        // 代理二，轮询
        System.out.println("期望：轮询");
        IGunService gunService = beanFactory.getBean(IGunService.class);
        gunService.shoot();
        gunService.shoot();
        gunService.shoot();
    }

    @Test
    void noInterface(){
        System.out.println("期望：路由");
        OrgFacade facade = beanFactory.getBean(OrgFacade.class);
        facade.print();
        facade.print();
        OrgStrategyProxy.routeKey.set("school");
        facade.print();
        facade.print();
        OrgStrategyProxy.routeKey.set("hosp");
        facade.print();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
