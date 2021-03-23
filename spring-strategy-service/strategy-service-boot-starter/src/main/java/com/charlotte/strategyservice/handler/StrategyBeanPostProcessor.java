package com.charlotte.strategyservice.handler;

import com.charlotte.strategyservice.annotation.StrategyMaster;
import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import com.charlotte.strategyservice.proxy.StrategyRouteHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.util.ClassUtils;

/**
 * @author Charlotte
 */
@Slf4j
public class StrategyBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StrategyRouteHelper.isMaster(bean)) {
            return createProxy(bean);
        }
        return bean;
    }

    public Object createProxy(Object bean) {
        StrategyMaster strategyMaster = bean.getClass().getAnnotation(StrategyMaster.class);
        Class<? extends AbstractStrategyProxy> proxyClass = strategyMaster.proxy();
        ObjectProvider<? extends AbstractStrategyProxy> beanProvider = beanFactory.getBeanProvider(proxyClass);
        AbstractStrategyProxy proxy = beanProvider.getIfAvailable();
        if(proxy == null){
            throw new NoSuchBeanDefinitionException(proxyClass);
        }
        proxy.setBean(bean);
        proxy.setBeanFactory(beanFactory);

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(ClassUtils.getAllInterfaces(bean));
        enhancer.setSuperclass(AopUtils.getTargetClass(bean));
        enhancer.setCallback(proxy);
        enhancer.setCallbackType(proxy.getClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        Object proxyBean = enhancer.create();
        return proxyBean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

}
