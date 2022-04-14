package cn.cidea.lab.proxy;

import cn.cidea.lab.proxy.service.IAniService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LabProxyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LabProxyApplication.class, args);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        IAniService bean = context.getBean(IAniService.class);
        bean.run();
        return;
    }

}
