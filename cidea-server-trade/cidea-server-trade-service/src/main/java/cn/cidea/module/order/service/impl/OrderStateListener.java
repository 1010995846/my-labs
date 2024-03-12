package cn.cidea.module.order.service.impl;

import cn.cidea.module.order.dal.mysql.IOrderMapper;
import cn.cidea.module.order.dataobject.entity.Order;
import cn.cidea.module.order.dataobject.enums.TradeOrderState;
import cn.cidea.module.order.dataobject.enums.OrderStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: CIdea
 */
@Slf4j
@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener extends StateListenerImpl<TradeOrderState, OrderStateEvent> {

    @Resource
    private IOrderMapper orderMapper;

    // TODO CIdea: from状态校验
    // TODO CIdea: 条件
    // TODO CIdea: 目标状态怎么确定
    @OnTransition(source = "UNPAID", target = "WAIT_DELIVER")
    public void payTransition(Message<OrderStateEvent> message) {
        Order order = (Order) getModel(message);
        log.info("支付，状态机反馈信息：{}", message.getHeaders());
        if("0".equals(order.getId())){
            throw new RuntimeException("dasdasda");
        }
        //更新订单
        order.setStatus(TradeOrderState.UNDELIVERED);
        orderMapper.updateById(order);
        //TODO 其他业务
    }
    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public void deliverTransition(Message<OrderStateEvent> message) {
        Order order = (Order) getModel(message);
        log.info("发货，状态机反馈信息：{}", message.getHeaders());
        //更新订单
        order.setStatus(TradeOrderState.WAIT_RECEIVE);
        orderMapper.updateById(order);
        //TODO 其他业务
    }
    @OnTransition(source = "WAIT_RECEIVE", target = "COMPLETE")
    public void receiveTransition(Message<OrderStateEvent> message) {
        Order order = (Order) getModel(message);
        log.info("确认收货，状态机反馈信息：{}", message.getHeaders());
        //更新订单
        order.setStatus(TradeOrderState.COMPLETE);
        orderMapper.updateById(order);
        //TODO 其他业务
    }
}