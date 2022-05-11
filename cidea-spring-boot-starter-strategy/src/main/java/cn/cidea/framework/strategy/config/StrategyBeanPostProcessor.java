package cn.cidea.framework.strategy.config;

import cn.cidea.framework.strategy.annotation.StrategyMaster;
import cn.cidea.framework.strategy.proxy.AbstractStrategyProxy;
import cn.cidea.framework.strategy.proxy.StrategyRouteHelper;
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
        return proxyIfNecessary(bean);
    }

    private Object proxyIfNecessary(Object bean) {
        if(bean == null){
            return null;
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        StrategyMaster strategyMaster = targetClass.getAnnotation(StrategyMaster.class);
        if(strategyMaster == null){
            return bean;
        }
        Class<? extends AbstractStrategyProxy> proxyClass = strategyMaster.proxy();
        // 获取代理实例的原型
        ObjectProvider<? extends AbstractStrategyProxy> beanProvider = beanFactory.getBeanProvider(proxyClass);
        AbstractStrategyProxy proxy = beanProvider.getIfAvailable();
        if(proxy == null){
            throw new NoSuchBeanDefinitionException(proxyClass);
        }
        proxy.setBean(bean);

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass));
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(proxy);
        enhancer.setCallbackType(proxy.getClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        Object proxyBean = enhancer.create();
        // 获取masterClass或masterClass接口，并扫包，注册branchClass
        StrategyRouteHelper.registerBranch(targetClass);
        return proxyBean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

}
