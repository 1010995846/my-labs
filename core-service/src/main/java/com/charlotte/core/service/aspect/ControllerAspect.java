package com.charlotte.core.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlotte
 */
@Deprecated
@Aspect
@Component
public class ControllerAspect {

    private final static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

//    @Pointcut("execution(public * com.charlotte.core.service.controller.*Controller.*(..)))")
    @Pointcut("(execution(public * com.charlotte.core.service.web.*Controller.*(..)))" +
            "|| execution(public * com.charlotte.core.service.controller.*Controller.*(..))))" +
            "&& @target(com.charlotte.demo.proxy.scan2.HsfComponent)")
//            "&& @annotation(com.charlotte.demo.proxy.scan2.HsfComponent)")
//            "&& @args(com.charlotte.demo.proxy.scan2.HsfComponent)")
    public void controller(){
        logger.info("cut");
    }


    @Before("controller()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
//        url, method, ip
        logger.info("url = {}\tmethod = {}\tip = {}", request.getRequestURI(), request.getMethod(), request.getRemoteAddr());

        logger.info("class_method = {}", joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        logger.info("class_method_args = {}", args);
    }

    @After("controller()")
    public void doAfter(){
//        logger.info("after ++++++++++++++++++++++++++++++++++");
    }

    @AfterReturning(returning = "object", value = "controller()")
    public void afterReturning(Object object){
        logger.info("response = {}", object);
    }
}
