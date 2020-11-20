package com.charlotte.lab.proxy;

import lombok.Setter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class AniRouteProxy implements MethodInterceptor {

    public static Map<Class, List<Class>> classMap = new HashMap<>();

    private DefaultListableBeanFactory beanFactory;
    private Class interfaceClass;

    public static void add(Class clazz){
        Class[] interfaces = null;
        Class sup = clazz;
        while ((interfaces = sup.getInterfaces()).length == 0){
            sup = sup.getSuperclass();
        }
        Class anInterface = interfaces[0];
        List<Class> beans = classMap.get(anInterface);
        if(beans == null){
            beans = new ArrayList<>();
            classMap.put(anInterface, beans);
        }
        beans.add(clazz);
    }

    /**
     *
     * @param obj 执行类
     * @param method    执行方法
     * @param args   参数
     * @param methodProxy   代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println(Thread.currentThread() + ", 调用前。。。");
//        Object obj = methodProxy.invokeSuper(o, objects);
        Class<?> instanceClass = classMap.get(interfaceClass).get(0);
        String beanName = "cat";
        Object instance = null;
        if(!beanFactory.containsBean(beanName)) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(instanceClass);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
        instance = beanFactory.getBean(beanName);

        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }

        Method targetMethod = instanceClass.getMethod(method.getName(), parameterTypes);
        Object invoke = targetMethod.invoke(instance, args);
        System.out.println("调用后。。。");
        return invoke;
    }
}
