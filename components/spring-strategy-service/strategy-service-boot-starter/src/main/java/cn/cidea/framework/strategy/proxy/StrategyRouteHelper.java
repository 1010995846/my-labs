package cn.cidea.framework.strategy.proxy;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.annotation.StrategyBranches;
import cn.cidea.framework.strategy.annotation.StrategyMaster;
import cn.cidea.framework.strategy.utils.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由工具类，分支类的扫描、注册，路由的映射、缓存
 * @author Charlotte
 */
@Slf4j
public class StrategyRouteHelper {

    /**
     * 路由映射配置
     * (port, routeKey): branchClass
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
    private static boolean enableCache = true;

    /**
     * 缓存，(routeKey, 代理的method): 执行bean和执行method的封装对象
     */
    private static Map<String, Map<Method, Invocation>> beanCache = new ConcurrentHashMap<>(16);

    public static void addBranchClass(Class clazz) {
        List<StrategyBranch> strategyBranchList = getStrategyBranches(clazz.getAnnotations());
        if (CollectionUtils.isEmpty(strategyBranchList)) {
            return;
        }

        Class port = getPortClass(clazz);
        // 分支路由配置
        Map<String, Class> branchClassMap = serviceClassMap.get(port);
        if (branchClassMap == null) {
            branchClassMap = new HashMap<>();
            serviceClassMap.put(port, branchClassMap);
        }
        // 默认路由key：类名
        branchClassMap.put(clazz.getSimpleName(), clazz);
        // 注解配置的路由key
        for (StrategyBranch strategyBranch : strategyBranchList) {
            for (String key : strategyBranch.value()) {
                branchClassMap.put(key, clazz);
            }
        }
    }

    private static List<StrategyBranch> getStrategyBranches(Annotation[] annotations) {
        List<StrategyBranch> strategyBranchList = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if(annotation.annotationType().getName().contains("java.lang")){
                continue;
            } else if(annotation instanceof StrategyBranch){
                strategyBranchList.add((StrategyBranch) annotation);
            } else if(annotation instanceof StrategyBranches){
                strategyBranchList.addAll(Arrays.asList(((StrategyBranches) annotation).value()));
            } else {
                strategyBranchList.addAll(getStrategyBranches(annotation.annotationType().getAnnotations()));
            }
        }
        return strategyBranchList;
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

    public static Invocation getCache(String[] keys, Method method) {
        if(!enableCache){
            return null;
        }
        Invocation invocation;
        for (String key : keys) {
            if((invocation = getCache(key, method)) != null){
                return invocation;
            }
        }
        return null;
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
    public static Class getPortClass(Class clazz) {
        Class upper = clazz;
        Class strategyMainClassToUse = null;
        while (upper != null){
            if (isMaster(upper)) {
                strategyMainClassToUse = upper;
            }
            if(upper.getInterfaces().length != 0){
                // 优先返回接口
                return upper.getInterfaces()[0];
            }
            upper = upper.getSuperclass();
        }
        if(strategyMainClassToUse != null){
            // 无接口时优先返回main注解的上级类
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
        Class portClass = getPortClass(clazz);
        Map<String, Class> branchClassMap = serviceClassMap.get(portClass);
        if (branchClassMap == null) {
            return null;
        }
        return branchClassMap.get(routeKey);
    }

    public static boolean isMaster(Object bean) {
        Class realClass = AopUtils.getTargetClass(bean);
        return isMaster(realClass);
    }

    public static boolean isMaster(Class clazz){
        return clazz.getAnnotation(StrategyMaster.class) != null;
    }

    public static boolean isBranch(Object bean){
        Class realClass = AopUtils.getTargetClass(bean);
        return isBranch(realClass);
    }

    public static boolean isBranch(Class clazz){
        return AnnotationUtils.findAnnotation(clazz, StrategyBranch.class) != null
                || AnnotationUtils.findAnnotation(clazz, StrategyBranches.class) != null;
    }

    public static void setEnableCache(boolean enableCache) {
        StrategyRouteHelper.enableCache = enableCache;
        clearCache();
    }

    public static void clearCache() {
        beanCache.clear();
    }

    public static void registerBranch(Class<?> masterClass) {
        // 获取策略的端口
        Class portClass = getPortClass(masterClass);
        String className = portClass.getName();
        // 根据端口获取包路径，也是上文使用说明中，分支类要在接口包下的原因
        String[] basePackages = new String[]{className.substring(0, className.lastIndexOf('.'))};

        // 过滤器匹配Branch
        List<TypeFilter> includeFilters = new ArrayList<>();
        includeFilters.add(new AbstractClassTestingTypeFilter() {
            @Override
            public boolean match(ClassMetadata metadataReader) {
                try {
                    Class clazz = ClassUtils.forName(metadataReader.getClassName(), portClass.getClassLoader());
                    return isBranch(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        List<TypeFilter> excludeFilters = new ArrayList<>();

        // 包工具类，扫描包并返回符合条件的Class，具体实现请查阅源码
        Set<Class<?>> candidates = PackageUtils.scanPackages(basePackages, portClass.getClassLoader(), includeFilters, excludeFilters);
        if (candidates.isEmpty()) {
            log.info("扫描指定包[{}]时未发现符合条件的分支类", basePackages.toString());
            return;
        }

        for (Class<?> candidate : candidates) {
            addBranchClass(candidate);
        }
    }

    public static class Invocation {

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