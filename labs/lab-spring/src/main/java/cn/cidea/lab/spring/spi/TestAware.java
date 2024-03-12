package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * Aware类型，可以拿到对应的实例，应用比较多
 * {@link org.springframework.context.support.ApplicationContextAwareProcessor#postProcessBeforeInitialization(Object, String)}
 * {@link org.springframework.context.support.ApplicationContextAwareProcessor#invokeAwareInterfaces(Object)}
 * @author: CIdea
 */
@Slf4j
@Component
public class TestAware implements BeanFactoryAware {
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("Custom setBeanFactory");
    }
}
