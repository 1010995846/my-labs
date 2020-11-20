package com.charlotte.lab.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DogAspect {

    @Around("execution(public void com.charlotte.strategyservicebootstarter.Dog.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("before");
        Object obj = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        System.out.println("after");
        return obj;
    }

}
