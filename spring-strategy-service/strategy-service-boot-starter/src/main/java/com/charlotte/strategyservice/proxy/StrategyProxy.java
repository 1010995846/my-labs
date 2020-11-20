package com.charlotte.strategyservice.proxy;

import com.charlotte.strategyservice.annotation.StrategyMain;
import com.charlotte.strategyservice.utils.ClassHelper;
import com.google.common.base.CaseFormat;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public abstract class StrategyProxy implements MethodInterceptor {

    private static final String STRATEGY_ROUTE_SERVICE_SUFFIX = "@Strategy";

    @Setter
    protected DefaultListableBeanFactory beanFactory;

    protected Object bean;
    /**
     * 最上级接口类/基类，用于策略组的类型声明和匹配
     */
    protected Class upperClass;

    // 主类
    protected Class strategyMainClass;

    /**
     * @param obj         执行类
     * @param method      执行方法
     * @param args        参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.debug("调用{}#{}({})", obj.getClass(), method.getName(), Arrays.toString(args));
        String key = getKey();
        StrategyRouteHelper.Invocation invocationToUse = null;
        Object result;
        if ((invocationToUse = StrategyRouteHelper.getCache(key, method)) != null) {
            // 命中缓存
            result = invocationToUse.invoke(args);
        } else {
            Object beanToUse;
            Method methodToUse;
            Class serviceClassToUse = StrategyRouteHelper.getBranchClass(obj.getClass(), key);
            if (serviceClassToUse == null) {
                // 使用主类的作为默认
                beanToUse = bean;
                methodToUse = method;
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

                Class[] parameterTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        continue;
                    }
                    parameterTypes[i] = args[i].getClass();
                }
                methodToUse = MethodUtils.getMatchingMethod(serviceClassToUse, method.getName(), parameterTypes);
            }
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            result = invocationToUse.invoke(args);
            StrategyRouteHelper.cacheBean(key, method, invocationToUse);
        }
        log.debug("调用完成。。。");
        return result;
    }

    public static boolean isProxy(Object bean, String beanName) {
        return beanName.contains(STRATEGY_ROUTE_SERVICE_SUFFIX);
    }

    public static boolean isTarget(Object bean) {
        Class realClass = AopUtils.getTargetClass(bean);
        return realClass.getAnnotation(StrategyMain.class) != null;
    }

    public void setBean(Object bean) {
        this.bean = bean;
        Class<?> realClass = AopUtils.getTargetClass(bean);
        Class proxyInterface = ClassHelper.getFirstInterface(bean);
        if (proxyInterface != null) {
            upperClass = proxyInterface;
        } else {
            upperClass = realClass;
        }
        strategyMainClass = realClass;
    }

    protected abstract String getKey();
}
