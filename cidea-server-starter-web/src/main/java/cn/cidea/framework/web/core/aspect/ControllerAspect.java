package cn.cidea.framework.web.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlotte
 */
@Aspect
@Slf4j
public class ControllerAspect {

    @Pointcut("(execution(public * cn.cidea..*.controller.*Controller.*(..)))"
//            + "&& @target(com.charlotte.demo.proxy.scan2.HsfComponent)")
//            + "&& @annotation(com.charlotte.demo.proxy.scan2.HsfComponent)")
           )
    public void controller(){
        log.info("cut");
    }

    @Before("controller()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
//        url, method, ip
        log.info("url = {}\tmethod = {}\tip = {}", request.getRequestURI(), request.getMethod(), request.getRemoteAddr());

        log.info("class_method = {}", joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        log.info("class_method_args = {}", args);
    }

    @After("controller()")
    public void doAfter(){
//        logger.info("after ++++++++++++++++++++++++++++++++++");
    }

    @AfterReturning(returning = "object", value = "controller()")
    public void afterReturning(Object object){
        log.info("response = {}", object);
    }
}
