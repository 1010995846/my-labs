package cn.cidea.framework.strategy.core.factory.proxy;

import cn.cidea.framework.strategy.core.IStrategyRoute;
import cn.cidea.framework.strategy.core.annotation.StrategyPort;
import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.core.annotation.StrategyBranches;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import cn.cidea.framework.strategy.core.factory.support.StrategyRegistry;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责代理实现
 * // TODO 因为是从FactoryBean创建的，spring配置类不生效，初始化无法利用InitializingBean
 * @author Charlotte
 */
public class StrategyProxy implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(StrategyProxy.class);

    private Class<?> portClass;

    private Class<? extends IStrategyRoute> routeClass;

    private DefaultListableBeanFactory beanFactory;

    private Object masterBean;

    /**
     * 分支实例
     */
    private Map<String, Object> branchBean;

    public StrategyProxy(Class<?> portClass, DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.portClass = portClass;
        StrategyPort annotation = AnnotationUtils.findAnnotation(portClass, StrategyPort.class);
        Assert.notNull(annotation, "strategy class has not @Strategy");
        this.routeClass = annotation.route();
    }

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
        log.debug("call: {}#{}({})", obj.getClass(), method.getName(), Arrays.toString(method.getParameterTypes()));
        // 获取routeKey。getRouteKeys()是抽象方法，用于重写，提供自定义的获取方案
        IStrategyRoute route = beanFactory.getBean(routeClass);
        Assert.notNull(route, "not bean of IStrategyRoute");
        tryInit();

        String[] routeKeys = route.getRouteKeys(obj, method, args, methodProxy);
        if(routeKeys == null){
            // 默认，避免NPE
            routeKeys = new String[]{};
        }
        log.debug("routeKeys = {}", Arrays.toString(routeKeys));
        // 待执行bean和method的封装对象
        Invocation invocationToUse;
        // 尝试获取缓存
        if ((invocationToUse = StrategyRegistry.getCache(routeKeys, method)) == null) {
            // 无缓存，开始解析
            // 尝试映射到对应的branchClass
            for (String routeKey : routeKeys) {
                if(routeKey == null){
                    continue;
                }
                Object beanToUse = branchBean.get(routeKey);
                if(beanToUse == null){
                    continue;
                }
                Method methodToUse = MethodUtils.getMatchingAccessibleMethod(
                        beanToUse.getClass(), method.getName(), method.getParameterTypes());
                if (methodToUse == null) {
                    continue;
                }
                invocationToUse = new Invocation(methodToUse, beanToUse);
                StrategyRegistry.cacheBean(routeKey, method, invocationToUse);
                break;
            }
        }
        if (invocationToUse == null) {
            if(masterBean == null){
                throw new IllegalAccessException("strategy `" + portClass.getClass().getName() + "` has not master");
            }
            log.debug("call master service。");

            Method methodToUse = MethodUtils.getMatchingAccessibleMethod(
                    masterBean.getClass(), method.getName(), method.getParameterTypes());
            invocationToUse = new Invocation(methodToUse, masterBean);
            StrategyRegistry.cacheBean(routeKeys, method, invocationToUse);
        }
        Object result = invocationToUse.invoke(args);
        log.debug("strategy proxy finished。");
        return result;
    }

    private void tryInit() {
        if (branchBean != null) {
            return;
        }
        synchronized (this) {
            if (branchBean != null) {
                return;
            }
            branchBean = new HashMap<>();
            Map<String, ?> beansOfType = beanFactory.getBeansOfType(portClass);
            branchBean.putAll(beansOfType);
            for (Object bean : beansOfType.values()) {
                if(bean.getClass().getAnnotation(StrategyMaster.class) != null){
                    if(masterBean != null){
                        throw new AnnotationConfigurationException("strategy `" + portClass.getClass().getName() + "` has duplicate master");
                    }
                    masterBean = bean;
                }
                List<StrategyBranch> branchList = getBranchAnnotations(bean.getClass());
                if (branchList == null) {
                    continue;
                }
                for (StrategyBranch branch : branchList) {
                    if (branch.value() == null) {
                        continue;
                    }
                    for (String key : branch.value()) {
                        if (branchBean.containsKey(key)) {
                            log.warn("strategy branch key `{}` conflict", key);
                        }
                        branchBean.put(key, bean);
                    }
                }
            }
        }
    }

    private List<StrategyBranch> getBranchAnnotations(Class branch) {
        List<StrategyBranch> strategyBranchList = new ArrayList<>();
        for (Annotation annotation : branch.getAnnotations()) {
            if (annotation.annotationType().getName().contains("java.lang")) {
                continue;
            } else if (annotation instanceof StrategyBranch) {
                strategyBranchList.add((StrategyBranch) annotation);
            } else if (annotation instanceof StrategyBranches) {
                strategyBranchList.addAll(Arrays.asList(((StrategyBranches) annotation).value()));
            } else {
                strategyBranchList.addAll(getBranchAnnotations(annotation.annotationType()));
            }
        }
        return strategyBranchList;
    }

    public class Invocation {

        private final Object bean;

        private final Method method;

        public Invocation(Method method, Object bean) {
            this.method = method;
            this.bean = bean;
        }

        public Object invoke(Object... args) throws Throwable {
            try {
                return method.invoke(bean, args);
            } catch (InvocationTargetException ite) {
                throw ite.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
