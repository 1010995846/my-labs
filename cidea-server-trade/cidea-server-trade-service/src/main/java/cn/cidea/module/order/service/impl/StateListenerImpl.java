package cn.cidea.module.order.service.impl;

import cn.cidea.module.order.dataobject.entity.Order;
import cn.cidea.module.order.dataobject.enums.OrderStateEvent;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author: CIdea
 */
@Slf4j
public class StateListenerImpl<S, E> {

    @Resource
    @Getter
    protected StateMachine<S, E> stateMachine;
    @Resource
    protected StateMachinePersister<S, E, Serializable> persister;

    /**
     * 发送订单状态转换事件
     * synchronized修饰保证这个方法是线程安全的
     *
     * @param model
     * @param event
     * @return
     */
    public synchronized boolean sendEvent(Model model, E event) {
        try {
            //启动状态机
            stateMachine.start();
            //尝试恢复状态机状态
            persister.restore(stateMachine, model.pkVal());
            Message<E> message = MessageBuilder.withPayload(event).setHeader("model", model).build();
            // 只是是否接收，接收完的处理结果（包含异常）不在此
            boolean accepted = stateMachine.sendEvent(message);
            if (!accepted) {
                // 没有对应的事件
                return false;
            }
            Exception exception = getException(model.pkVal());
            if (exception != null) {
                // throw exception;
                return false;
            }
            //持久化状态机状态
            persister.persist(stateMachine, model.pkVal());
            return true;
        } catch (Exception e) {
            log.error("操作失败", e);
            return false;
        } finally {
            stateMachine.stop();
        }
    }

    public Model getModel(Message<E> message) {
        return (Model) message.getHeaders().get("model");
    }

    public void setException(Serializable id, Exception e) {
        stateMachine.getExtendedState().getVariables().put(id, e);
    }

    public Exception getException(Serializable id) {
        return (Exception) stateMachine.getExtendedState().getVariables().get(id);
    }
}
