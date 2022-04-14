package cn.cidea.framework.strategy.proxy;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.annotation.StrategyRoute;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 负责代理实现
 *
 * @author Charlotte
 */
@Slf4j
@StrategyRoute
public abstract class AbstractStrategyProxy implements MethodInterceptor, BeanFactoryAware {

    private static final String STRATEGY_ROUTE_SERVICE_SUFFIX = "@Strategy";

    private static final String[] NULL_KEYS = new String[]{null};

    protected Object bean;

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
        if(routeKeys == null){
            routeKeys = NULL_KEYS;
        }
        log.debug("routeKeys = {}", Arrays.toString(routeKeys));
        // 待执行bean和method的封装对象
        StrategyRouteHelper.Invocation invocationToUse;
        // 尝试获取缓存
        if ((invocationToUse = StrategyRouteHelper.getCache(routeKeys, method)) == null) {
            // 无缓存，开始解析
            // 尝试映射到对应的branchClass
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            for (String routeKey : routeKeys) {
                Class serviceClassToUse = StrategyRouteHelper.getBranchClass(targetClass, routeKey);
                if(serviceClassToUse == null){
                    continue;
                }

                // 映射到了branchClass，进行初始化
                log.debug("branchClassToUse = {}", serviceClassToUse);
                String serviceNameToUse = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceClassToUse.getSimpleName()) + STRATEGY_ROUTE_SERVICE_SUFFIX;

                // 避免并发时branch重复注入spring
                Object beanToUse;
                if (!beanFactory.containsBean(serviceNameToUse)) {
                    synchronized (serviceClassToUse) {
                        if (!beanFactory.containsBean(serviceNameToUse)) {
                            // branch未注册时，注入spring，完成依赖
                            RootBeanDefinition beanDefinition = new RootBeanDefinition(serviceClassToUse);
                            beanFactory.registerBeanDefinition(serviceNameToUse, beanDefinition);
                        }
                    }
                }
                beanToUse = beanFactory.getBean(serviceNameToUse);

                Method methodToUse = MethodUtils.getMatchingMethod(
                        serviceClassToUse, method.getName(), method.getParameterTypes());
                invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
                StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
                break;
            }
        }
        if (invocationToUse == null){
            // 兜底的默认操作，路由不存在对应的branchClass
            log.debug("call master service。");
            // 调用获取默认bean和method的方法，默认为代理的mainBean和method，可重写修改
            Object beanToUse = getDefaultBeanToUse(obj, method, args, methodProxy, routeKeys);
            Method methodToUse = getDefaultMethodToUse(obj, method, args, methodProxy, routeKeys);
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            for (String routeKey : routeKeys) {
                StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
            }
        }
        // invoke()时不可锁class，防止循环调用锁死
        Object result = invocationToUse.invoke(args);
        log.debug("strategy proxy finished。");
        return result;
    }

    public final void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    protected Method getDefaultMethodToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return method;
    }

    protected Object getDefaultBeanToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return bean;
    }

    /**
     * 路由key，由用户重写自定义路由策略规则，匹配对应接口/父类下属的分支{@link StrategyBranch#value()}
     * @param obj
     * @param method
     * @param args
     * @param methodProxy
     * @return
     */
    protected abstract String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy);

}
