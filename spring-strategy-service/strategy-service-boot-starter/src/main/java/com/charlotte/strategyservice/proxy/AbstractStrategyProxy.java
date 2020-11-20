package com.charlotte.strategyservice.proxy;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 负责代理实现
 * @author Charlotte
 */
@Slf4j
public abstract class AbstractStrategyProxy implements MethodInterceptor {

    private static final String STRATEGY_ROUTE_SERVICE_SUFFIX = "@Strategy";

    private Object bean;

    protected DefaultListableBeanFactory beanFactory;

    /**
     * @param obj         执行类
     * @param method      执行方法
     * @param args        参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public final Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.debug("调用{}#{}({})", obj.getClass(), method.getName(), Arrays.toString(args));
        String routeKey = getRouteKey(obj, method, args, methodProxy);
        log.debug("route routeKey = {}", routeKey);
        StrategyRouteHelper.Invocation invocationToUse = null;
        Object result;
        if ((invocationToUse = StrategyRouteHelper.getCache(routeKey, method)) != null) {
            // 命中缓存
            result = invocationToUse.invoke(args);
        } else {
            Object beanToUse;
            Method methodToUse;
            Class serviceClassToUse = StrategyRouteHelper.getBranchClass(obj.getClass(), routeKey);
            if (serviceClassToUse == null) {
                // 使用主类的作为默认
                beanToUse = getDefaultBeanToUse(obj, method, args, methodProxy, routeKey);
                methodToUse = getDefaultMethodToUse(obj, method, args, methodProxy, routeKey);
            } else {
                String serviceNameToUse = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceClassToUse.getSimpleName());

                log.debug("serviceToUse = {}", serviceClassToUse);
                serviceNameToUse = serviceNameToUse + STRATEGY_ROUTE_SERVICE_SUFFIX;

                synchronized (serviceClassToUse) {
                    if (!beanFactory.containsBean(serviceNameToUse)) {
                        // 注册依赖
                        RootBeanDefinition beanDefinition = new RootBeanDefinition(serviceClassToUse);
                        beanFactory.registerBeanDefinition(serviceNameToUse, beanDefinition);
                    }
                    beanToUse = beanFactory.getBean(serviceNameToUse);
                    beanFactory.removeBeanDefinition(serviceNameToUse);
                }

                methodToUse = MethodUtils.getMatchingMethod(serviceClassToUse, method.getName(), method.getParameterTypes());
            }
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            result = invocationToUse.invoke(args);
            StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
        }
        log.debug("调用完成。。。");
        return result;
    }

    /**
     * 是否已代理
     * @param bean
     * @param beanName
     * @return
     */
    public static final boolean isProxy(Object bean, String beanName) {
        return beanName.contains(STRATEGY_ROUTE_SERVICE_SUFFIX);
    }

    public final void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public final void setBean(Object bean) {
        this.bean = bean;
    }

    protected Method getDefaultMethodToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String routeKey) {
        return method;
    }

    protected Object getDefaultBeanToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String routeKey){
        return bean;
    };

    protected abstract String getRouteKey(Object obj, Method method, Object[] args, MethodProxy methodProxy);
}
