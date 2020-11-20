package com.charlotte.strategyservice.proxy;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.annotation.StrategyMain;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责检测、路由
 * @author Charlotte
 */
@Slf4j
public class StrategyRouteHelper {

    /**
     * 接口/基类和分支类（不包含主类）
     */
    private static final Map<Class, Map<String, Class>> serviceClassMap = new HashMap<>(32);
    private static final String NULL_KEY = "null";

    private static boolean enableCache = true;
    /**
     * 缓存，key-调用方法: 实际执行的封装对象
     */
    private static Map<String, Map<Method, Invocation>> beanCache = new ConcurrentHashMap<>(16);

    public static void addServiceClass(Class clazz) {
        StrategyBranch strategyBranch = (StrategyBranch) clazz.getDeclaredAnnotation(StrategyBranch.class);
        if (strategyBranch == null) {
            return;
        }
        Class upperClassToUse = getUpperClass(clazz);

        // 分支路由配置
        Map<String, Class> branchClassMap = serviceClassMap.get(upperClassToUse);
        if (branchClassMap == null) {
            branchClassMap = new HashMap<>();
            serviceClassMap.put(upperClassToUse, branchClassMap);
        }
        // 默认路由key：类名
        branchClassMap.put(clazz.getSimpleName(), clazz);
        // 注解配置的路由key
        for (String key : strategyBranch.value()) {
            branchClassMap.put(key, clazz);
        }
    }

    public static void cacheBean(String key, Method method, Invocation invocation) {
        if(!enableCache){
            return;
        }
        if(key == null){
            key = NULL_KEY;
        }
        Map<Method, Invocation> methodInvocationMap = beanCache.get(key);
        if (methodInvocationMap == null) {
            methodInvocationMap = new ConcurrentHashMap<>(64);
            beanCache.put(key, methodInvocationMap);
        }
        methodInvocationMap.put(method, invocation);
    }

    public static void clearCache() {
        beanCache.clear();
    }

    public static Invocation getCache(String key, Method method) {
        if(!enableCache){
            return null;
        }
        if(key == null){
            key = NULL_KEY;
        }
        Map<Method, Invocation> invocationMap = beanCache.get(key);
        if (invocationMap == null) {
            return null;
        }
        return invocationMap.get(method);
    }

    /**
     * 根据分支类获取上级主流程类，若实现接口则返回第一个接口作为业务接口
     * @param clazz
     * @return
     */
    public static Class getUpperClass(Class clazz) {
        // 确定主类
        Class sup = clazz;
        Class strategyMainClassToUse = null;
        while (sup != null) {
            if (isTarget(sup)) {
                strategyMainClassToUse = sup;
                break;
            }
            sup = sup.getSuperclass();
        }

        if (strategyMainClassToUse == null) {
            return null;
        }
        // 返回主类的第一个接口或直接返回主类
        Class[] interfaces = strategyMainClassToUse.getInterfaces();
        if (interfaces.length != 0) {
            return interfaces[0];
        } else {
            return strategyMainClassToUse;
        }
    }


    public static Class getBranchClass(Class clazz, String routeKey) {
        Class upperClass = StrategyRouteHelper.getUpperClass(clazz);
        Map<String, Class> classMap = serviceClassMap.get(upperClass);
        if (classMap == null) {
            return null;
        }
        return classMap.get(routeKey);
    }

    public static boolean isTarget(Object bean) {
        Class realClass = AopUtils.getTargetClass(bean);
        return isTarget(realClass);
    }

    public static boolean isTarget(Class clazz){
        return clazz.getAnnotation(StrategyMain.class) != null;
    }

    public static boolean isBranch(Class clazz){
        return clazz.getAnnotation(StrategyBranch.class) != null;
    }

    public static void setEnableCache(boolean enableCache) {
        StrategyRouteHelper.enableCache = enableCache;
    }

    public static class Invocation {

        private final Method method;

        private final Object target;

        private final Object[] args;

        public Invocation(Method method, Object target, Object... args) {
            this.method = method;
            this.target = target;
            this.args = args;
        }

        public Object invoke() throws Throwable {
            return invoke(args);
        }

        public Object invoke(Object... args) throws Throwable {
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException ite) {
                throw ite.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
