package com.charlotte.strategyservice.handle;

import com.charlotte.strategyservice.annotation.StrategyMain;
import com.charlotte.strategyservice.proxy.StrategyProxy;
import com.charlotte.strategyservice.utils.ClassHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.lang.Nullable;

@Slf4j
public class StrategyBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (StrategyProxy.isTarget(bean) && !StrategyProxy.isProxy(bean, beanName)) {
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

        StrategyProxy proxy = createProxyInstance();
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

    public StrategyProxy createProxyInstance() {
        // TODO Charlotte: 2020/11/10 proxy自定义化
        StrategyProxy strategyProxy = beanFactory.getBean(StrategyProxy.class);
        strategyProxy = new StrategyProxy() {
            int i = 0;
            @Override
            protected String getKey() {
                switch (i++%3){
                    case 1:
                        return "school";
                    case 2:
                        return "hosp";
                    default:
                        return null;
                }
            }
        };
        return strategyProxy;
    }
}
