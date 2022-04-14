package cn.cidea.lab.proxy.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Aspect
@Component
public class ServiceAspect {

    @Around("execution(public void com.charlotte.lab.proxy.service.*Service.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("AOP before");
        Object obj = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        System.out.println("AOP after");
        return obj;
    }

}
