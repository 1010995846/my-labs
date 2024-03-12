package cn.cidea.module.order.dataobject.enums.statemachine;

import cn.cidea.module.order.dataobject.enums.TradeOrderState;
import cn.cidea.module.order.dataobject.enums.OrderStateEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * @author: CIdea
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<TradeOrderState, OrderStateEvent> {
    /**
     * 配置状态
     *
     * @param configurer
     * @throws Exception
     */
    public void configure(StateMachineStateConfigurer<TradeOrderState, OrderStateEvent> configurer) throws Exception {
        configurer
                .withStates()
                .initial(TradeOrderState.UNPAID)
                .states(EnumSet.allOf(TradeOrderState.class));
    }
    /**
     * 配置状态转换事件关系
     *
     * @param transitions
     * @throws Exception
     */
    public void configure(StateMachineTransitionConfigurer<TradeOrderState, OrderStateEvent> transitions) throws Exception {
        transitions
                //支付事件:待支付-》待发货
                .withExternal().source(TradeOrderState.UNPAID).target(TradeOrderState.UNDELIVERED).event(OrderStateEvent.PAYED)
                .and()
                //发货事件:待发货-》待收货
                .withExternal().source(TradeOrderState.UNDELIVERED).target(TradeOrderState.WAIT_RECEIVE).event(OrderStateEvent.DELIVERY)
                .and()
                //收货事件:待收货-》已完成
                .withExternal().source(TradeOrderState.WAIT_RECEIVE).target(TradeOrderState.COMPLETE).event(OrderStateEvent.RECEIVED)
                .and()
                .withExternal().source(TradeOrderState.COMPLETE).target(TradeOrderState.CLOSED).event(OrderStateEvent.REFUND)
                .and()
                .withExternal().source(TradeOrderState.UNPAID).target(TradeOrderState.CLOSED).event(OrderStateEvent.CANCEL)
                .and()
                .withExternal().source(TradeOrderState.UNPAID).target(TradeOrderState.CLOSED).event(OrderStateEvent.EXPIRE);
    }
}