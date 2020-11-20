package com.charlotte.lab.proxy;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
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
public class RouteProxy implements MethodInterceptor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;
    private Class interfaceClass;
    private List<Class> serviceList = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();

    public RouteProxy() {
    }

    public RouteProxy(DefaultListableBeanFactory beanFactory, Class interfaceClass) {
        this.beanFactory = beanFactory;
        this.interfaceClass = interfaceClass;
    }

    public void addService(Class clazz){
        serviceList.add(clazz);
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
        Class<?> instanceClass = serviceList.get(0);
        String beanName = instanceClass.getSimpleName();
        Object instance = null;
        if(!beanFactory.containsBean(beanName)) {
            beanFactory.registerBeanDefinition(beanName, new RootBeanDefinition(instanceClass));
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

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
