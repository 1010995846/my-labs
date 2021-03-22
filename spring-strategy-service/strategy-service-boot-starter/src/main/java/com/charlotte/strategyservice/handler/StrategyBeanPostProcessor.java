package com.charlotte.strategyservice.handler;

import com.charlotte.strategyservice.utils.ClassHelper;
import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import com.charlotte.strategyservice.proxy.StrategyRouteHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author Charlotte
 */
@Slf4j
public class StrategyBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (StrategyRouteHelper.isMaster(bean) && !StrategyRouteHelper.isBranch(bean)) {
                return createProxy(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public Object createProxy(Object bean) {
        Class<?> realClass = AopUtils.getTargetClass(bean);
        Class proxyInterface = ClassHelper.getFirstInterface(bean);
//        StrategyMain strategyMain = realClass.getAnnotation(StrategyMain.class);
//        ObjectProvider<? extends AbstractStrategyProxy> beanProvider = beanFactory.getBeanProvider(strategyMain.proxy());
        ObjectProvider<? extends AbstractStrategyProxy> beanProvider = beanFactory.getBeanProvider(AbstractStrategyProxy.class);
        AbstractStrategyProxy proxy = beanProvider.getIfAvailable();
        proxy.setBean(bean);
        proxy.setBeanFactory(beanFactory);

        Enhancer enhancer = new Enhancer();
        if (proxyInterface != null) {
            enhancer.setInterfaces(new Class[]{proxyInterface});
        }
        enhancer.setSuperclass(realClass);
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
