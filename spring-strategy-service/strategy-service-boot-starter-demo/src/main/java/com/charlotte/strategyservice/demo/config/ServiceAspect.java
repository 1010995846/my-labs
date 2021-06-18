package com.charlotte.strategyservice.demo.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Component
@Aspect
public class ServiceAspect {

    @Around("execution(public * com.charlotte.strategyservice.demo.service..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("---------------");
        return joinPoint.proceed();
    }

}
