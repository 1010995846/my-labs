package com.charlotte.lab.proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RegisterProxy implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 代理业务实现类，即interfaceClass
        // jdk代理仅支持接口，无法继承类
        Class<?> crudService = Object.class;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(crudService);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
        // 代理执行类
        definition.setBeanClass(JdkProxy.class);
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        registry.registerBeanDefinition(crudService.getSimpleName(), definition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
