package cn.cidea.framework.strategy.core.factory.support;

import cn.cidea.framework.strategy.core.factory.proxy.StrategyProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Charlotte
 */
public class StrategyRegistry {

    /**
     * key为null时的默认key值
     */
    private static final String NULL_KEY = "null";
    private static final String[] NULL_KEYS = new String[]{NULL_KEY};

    /**
     * 分支执行方法缓存，(routeKey, 代理的method): 执行bean和执行method的封装对象
     */
    private static final Map<String, Map<Method, StrategyProxy.Invocation>> knownInvocationCache = new ConcurrentHashMap<>(16);

    public static void cacheBean(String[] keys, Method method, StrategyProxy.Invocation invocation) {
        if(keys == null || keys.length == 0){
            keys = NULL_KEYS;
        }
        for (String key : keys) {
            cacheBean(key, method, invocation);
        }
    }

    public static void cacheBean(String key, Method method, StrategyProxy.Invocation invocation) {
        if (key == null) {
            key = NULL_KEY;
        }
        Map<Method, StrategyProxy.Invocation> methodInvocationMap = knownInvocationCache.get(key);
        if (methodInvocationMap == null) {
            methodInvocationMap = new ConcurrentHashMap<>(64);
            knownInvocationCache.put(key, methodInvocationMap);
        }
        methodInvocationMap.put(method, invocation);
    }

    public static StrategyProxy.Invocation getCache(String[] keys, Method method) {
        if(keys == null || keys.length == 0){
            keys = NULL_KEYS;
        }
        StrategyProxy.Invocation invocation;
        for (String key : keys) {
            if ((invocation = getCache(key, method)) != null) {
                return invocation;
            }
        }
        return null;
    }

    public static StrategyProxy.Invocation getCache(String key, Method method) {
        if (key == null) {
            key = NULL_KEY;
        }
        Map<Method, StrategyProxy.Invocation> invocationMap = knownInvocationCache.get(key);
        if (invocationMap == null) {
            return null;
        }
        return invocationMap.get(method);
    }

    public static void cleanCache(){
        knownInvocationCache.clear();
    }
}
