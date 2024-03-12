package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * 继承了BeanPostProcess接口，区别如下：
 * BeanPostProcess接口只在bean的初始化阶段进行扩展（注入spring上下文前后），而InstantiationAwareBeanPostProcessor接口在此基础上增加了3个方法，把可扩展的范围增加了实例化阶段和属性注入阶段
 * 使用场景：这个扩展点非常有用 ，无论是写中间件和业务中，都能利用这个特性。比如对实现了某一类接口的bean在各个生命期间进行收集，或者对某个类型的bean进行统一的设值等等。
 *
 * @author: CIdea
 */
@Slf4j
@Component
public class TestInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private Set<String> set = new HashSet<>();

    /**
     * 实例化bean之前，相当于new这个bean之前
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (set.add("postProcessBeforeInstantiation")) {
            log.info("Custom postProcessBeforeInstantiation");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    /**
     * 实例化bean之后，相当于new这个bean之后
     * 一般在这
     * 应用：策略化用代理bean替换返回的bean
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (set.add("postProcessAfterInstantiation")) {
            log.info("Custom postProcessAfterInstantiation");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (set.add("postProcessProperties")) {
            log.info("Custom postProcessProperties");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }

    /**
     * 初始化bean之前，相当于把bean注入spring上下文之前
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (set.add("postProcessBeforeInitialization")) {
            log.info("Custom postProcessBeforeInitialization");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    /**
     * 初始化bean之后，相当于把bean注入spring上下文之后
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (set.add("postProcessAfterInitialization")) {
            log.info("Custom postProcessAfterInitialization");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
