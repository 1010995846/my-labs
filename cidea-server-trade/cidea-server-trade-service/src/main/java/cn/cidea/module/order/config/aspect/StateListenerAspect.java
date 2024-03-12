package cn.cidea.module.order.config.aspect;

import cn.cidea.module.order.service.impl.StateListenerImpl;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.stereotype.Component;

/**
 * @author: CIdea
 */
@Slf4j
@Aspect
@Component
public class StateListenerAspect {

    @Around("target(cn.cidea.module.order.service.impl.StateListenerImpl) && @annotation(onTransition)")
    public Object logResultAround(ProceedingJoinPoint pjp, OnTransition onTransition) throws Throwable {
        //获取参数
        Object[] args = pjp.getArgs();
        Message message = (Message) args[0];

        StateListenerImpl listener = (StateListenerImpl) pjp.getTarget();
        Model model = listener.getModel(message);
        Exception exception = null;
        try {
            Object returnVal = pjp.proceed();
            return returnVal;
        } catch (Exception e) {
            // log.error("StateListenerAspect Error", e);
            exception = e;
            throw e;
        } finally {
            listener.setException(model.pkVal(), exception);
        }
    }
}
