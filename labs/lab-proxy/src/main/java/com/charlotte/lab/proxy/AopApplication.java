package com.charlotte.lab.proxy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.charlotte.demo.aop")
@EnableAspectJAutoProxy
public class AopApplication {


    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(AopApplication.class, args);
//        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
//        Ani bean = context.getBean(Ani.class);
//        bean.run();
//        return;
        t();
    }

    public static void t(){
        try {
            return;
        }finally {
            System.out.println("dasdads");
        }
    }

}
