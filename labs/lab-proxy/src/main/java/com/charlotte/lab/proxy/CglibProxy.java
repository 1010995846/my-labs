package com.charlotte.lab.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {
    /**
     *
     * @param o 执行类
     * @param method    执行方法
     * @param objects   参数
     * @param methodProxy   代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用前。。。");
        Object obj = methodProxy.invokeSuper(o, objects);
        System.out.println("调用后。。。");
        return obj;
    }

}
