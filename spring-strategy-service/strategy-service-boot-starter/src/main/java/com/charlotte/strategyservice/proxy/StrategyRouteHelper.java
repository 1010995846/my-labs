package com.charlotte.strategyservice.proxy;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.annotation.StrategyMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由工具类，负责路由映射、缓存
 * @author Charlotte
 */
@Slf4j
public class StrategyRouteHelper {

    /**
     * 路由映射配置
     * (mainClass/implClass, routeKey): branchClass
     */
    private static final Map<Class, Map<String, Class>> serviceClassMap = new HashMap<>(32);

    /**
     * 当routeKey为空时，默认的key
     */
    private static final String NULL_KEY = "null";

    /**
     * 是否允许缓存，默认开启，调试时可关闭
     * @see #setEnableCache(boolean)
     */
    private static boolean enableCache = false;

    /**
     * 缓存，(routeKey, 代理的method): 执行bean和执行method的封装对象
     */
    private static Map<Object, Map<Method, Invocation>> beanCache = new ConcurrentHashMap<>(16);

    public static void addBranchClass(Class clazz) {
        StrategyBranch strategyBranch = AnnotationUtils.findAnnotation(clazz, StrategyBranch.class);
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

    public static void cacheBean(String[] keys, Method method, Invocation invocation) {
        if(!enableCache){
            return;
        }
        cacheBean((Object) keys, method, invocation);
        for (String key : keys) {
            cacheBean(key, method, invocation);
        }
    }

    private static void cacheBean(Object key, Method method, Invocation invocation) {
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

    public static Invocation getCache(Object key, Method method) {
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
        Class upper = clazz;
        Class strategyMainClassToUse = null;
        while (upper != null){
            if (isMain(upper)) {
                strategyMainClassToUse = upper;
            }
            if(upper.getInterfaces().length != 0){
                // 优先返回接口
                return upper.getInterfaces()[0];
            }
            upper = upper.getSuperclass();
        }
        if(strategyMainClassToUse != null){
            // 无接口时返回main注解的上级类
            return strategyMainClassToUse;
        }
        return clazz;
    }

    /**
     * 根据clazz从serviceClassMap中获取main所在的branch域，并根据routeKey获取branch
     * @see #addBranchClass(Class) 方法添加branch时进行解析并加入serviceClassMap
     * @param clazz
     * @param routeKey
     * @return
     */
    public static Class getBranchClass(Class clazz, String routeKey) {
        Class upperClass = StrategyRouteHelper.getUpperClass(clazz);
        Map<String, Class> classMap = serviceClassMap.get(upperClass);
        if (classMap == null) {
            return null;
        }
        return classMap.get(routeKey);
    }

    public static boolean isMain(Object bean) {
        Class realClass = AopUtils.getTargetClass(bean);
        return isMain(realClass);
    }

    public static boolean isMain(Class clazz){
        return clazz.getAnnotation(StrategyMain.class) != null;
    }

    public static boolean isBranch(Object bean){
        Class realClass = AopUtils.getTargetClass(bean);
        return isBranch(realClass);
    }

    public static boolean isBranch(Class clazz){
        return AnnotationUtils.findAnnotation(clazz, StrategyBranch.class) != null;
    }

    public static void setEnableCache(boolean enableCache) {
        StrategyRouteHelper.enableCache = enableCache;
    }

    public static class Invocation {

        private final Method method;

        private final Object target;

        public Invocation(Method method, Object target) {
            this.method = method;
            this.target = target;
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
