package com.charlotte.lab.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            Class<?> base = method.getDeclaringClass().getSuperclass();
            Method routeMethod = base.getDeclaredMethod("getService");
            Object service = routeMethod.invoke(this);
            Method targetMethod = service.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            return targetMethod.invoke(service, args);
        }
        return method.invoke(this, args);
    }

}