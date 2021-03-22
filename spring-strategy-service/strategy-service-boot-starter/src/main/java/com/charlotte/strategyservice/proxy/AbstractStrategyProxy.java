package com.charlotte.strategyservice.proxy;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 负责代理实现
 *
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
        log.debug("call: {}#{}({})", obj.getClass(), method.getName(), Arrays.toString(method.getParameterTypes()));
        // 获取routeKey。getRouteKeys()是抽象方法，用于重写，提供自定义的获取方案
        String[] routeKeys = getRouteKeys(obj, method, args, methodProxy);
        log.debug("routeKeys = {}", Arrays.toString(routeKeys));
        StrategyRouteHelper.Invocation invocationToUse;
        // 尝试获取缓存
        if ((invocationToUse = StrategyRouteHelper.getCache(routeKeys, method)) != null) {
            // 命中缓存
            return invocationToUse.invoke(args);
        }
        // 无缓存，开始解析
        // 尝试映射到对应的branchClass
        Class serviceClassToUse;
        Object result;
        for (String routeKey : routeKeys) {
            serviceClassToUse = StrategyRouteHelper.getBranchClass(ClassUtils.getUserClass(obj.getClass()), routeKey);
            if(serviceClassToUse == null){
                continue;
            }
            // 映射到了branchClass，进行初始化
            log.debug("branchClassToUse = {}", serviceClassToUse);
            String serviceNameToUse = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceClassToUse.getSimpleName()) + STRATEGY_ROUTE_SERVICE_SUFFIX;

            // 避免并发时branch重复注入spring
            Object beanToUse;
            synchronized (serviceClassToUse) {
                if (!beanFactory.containsBean(serviceNameToUse)) {
                    // branch未注册时，注入spring，完成依赖
                    RootBeanDefinition beanDefinition = new RootBeanDefinition(serviceClassToUse);
                    beanFactory.registerBeanDefinition(serviceNameToUse, beanDefinition);
                }
                beanToUse = beanFactory.getBean(serviceNameToUse);
                // 移除，防止spring容器中存在多个实例，由于@Primy注解，不再需要保证接口的唯一实现
                // beanFactory.removeBeanDefinition(serviceNameToUse);
            }

            Method methodToUse = MethodUtils.getMatchingMethod(
                    serviceClassToUse, method.getName(), method.getParameterTypes());
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
        }
        if (invocationToUse == null){
            // 路由不存在对应的branchClass
            log.debug("call master service。");
            // 调用获取默认bean和method的方法，默认为代理的mainBean和method，可重写修改
            Object beanToUse = getDefaultBeanToUse(obj, method, args, methodProxy, routeKeys);
            Method methodToUse = getDefaultMethodToUse(obj, method, args, methodProxy, routeKeys);
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            for (String routeKey : routeKeys) {
                StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
            }
        }
        // invoke()时不可锁class
        result = invocationToUse.invoke(args);
        log.debug("strategy proxy finished。");
        return result;
    }

    public final void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public final void setBean(Object bean) {
        this.bean = bean;
    }

    protected Method getDefaultMethodToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return method;
    }

    protected Object getDefaultBeanToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return obj;
    }

    /**
     * 路由key，匹配对应接口/父类下属的分支{@link StrategyBranch#value()}
     *
     * @param obj
     * @param method
     * @param args
     * @param methodProxy
     * @return
     */
    protected abstract String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy);
}
