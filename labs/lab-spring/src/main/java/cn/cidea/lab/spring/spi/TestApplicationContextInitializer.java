package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 这是整个spring容器在刷新之前初始化ConfigurableApplicationContext的回调接口，简单来说，就是在容器刷新之前调用此类的initialize方法。
 * 这个点允许被用户自己扩展。用户可以在整个spring容器还没被初始化之前做一些事情。
 * 可以想到的场景可能为，在最开始激活一些配置，或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作。
 * 启用方式
 * 一、{@link SpringApplication#addInitializers(ApplicationContextInitializer[])}
 * 二、配置。context.initializer.classes=
 * 三、Spring SPI。org.springframework.context.ApplicationContextInitializer=
 *
 * 因为容器都还没初始化，用不了容器组件，需要手动添加到application里
 * @author: CIdea
 */
@Slf4j
public class TestApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        log.info("Custom ContextInitializer");
    }

}
