package cn.cidea.framework.chain;

import cn.cidea.framework.chain.service.IMsgChainService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = ChainServiceBootStarterDemoApplication.class)
@SpringBootTest(classes = ChainServiceBootStarterDemoApplication.class)
class ChainServiceBootStarterDemoApplicationTests implements BeanFactoryAware {

    private BeanFactory beanFactory;
    @Autowired
    private IMsgChainService msgService;

    @Test
    void contextLoads() {
        // 期望，轮着输出
        msgService.alter();
        System.out.println(msgService.hashCode());
        System.out.println(msgService.toString());
        System.out.println(msgService.getClass());
        return;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
