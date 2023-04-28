package cn.cidea.framework.strategy;

import cn.cidea.framework.strategy.config.DefaultDynamicStrategyRouter;
import cn.cidea.framework.strategy.facade.OrgFacade;
import cn.cidea.framework.strategy.service.IGunService;
import cn.cidea.framework.strategy.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StrategyDemoApplication.class)
class StrategyTests implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Test
    void polling() {
        System.out.println("期望：轮询");
        IGunService implBean = beanFactory.getBean(IGunService.class);
        for (int i = 0; i < 7; i++) {
            implBean.shoot();
        }
    }

    @Test
    void dynamic() {
        System.out.println("期望：路由");
        IOrgService implBean = beanFactory.getBean(IOrgService.class);
        System.out.println("school=============");
        DefaultDynamicStrategyRouter.routeKey.set("school");
        System.out.println(implBean.getName());// 打印school

        System.out.println("hosp=============");
        DefaultDynamicStrategyRouter.routeKey.set("hosp");
        System.out.println(implBean.getName());// 打印hosp
        System.out.println(implBean.getName());// 打印hosp

        System.out.println("ext=============");
        DefaultDynamicStrategyRouter.routeKey.set("ext");
        System.out.println(implBean.getName());// 打印ext

        System.out.println("defaultOrg=============");
        DefaultDynamicStrategyRouter.routeKey.remove();
        System.out.println(implBean.getName());// 打印defaultOrg

        System.out.println("plural=============");
        DefaultDynamicStrategyRouter.routeKey.set("p1");
        System.out.println(implBean.getName());// 打印plural
        DefaultDynamicStrategyRouter.routeKey.set("p2");
        System.out.println(implBean.getName());// 打印plural
    }

    @Test
    void multipleRoute(){
        // 代理一，指定路由
        System.out.println("期望：路由");
        IOrgService implBean = beanFactory.getBean(IOrgService.class);
        System.out.println("school=============");
        DefaultDynamicStrategyRouter.routeKey.set("school");
        System.out.println(implBean.getName());// 打印school
        System.out.println(implBean.getName());// 打印school

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
        System.out.println("default=============");
        DefaultDynamicStrategyRouter.routeKey.remove();
        facade.print();
        facade.print();
        DefaultDynamicStrategyRouter.routeKey.set("school");
        System.out.println("school==============");
        facade.print();
        facade.print();
        DefaultDynamicStrategyRouter.routeKey.set("hosp");
        System.out.println("hosp================");
        facade.print();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
