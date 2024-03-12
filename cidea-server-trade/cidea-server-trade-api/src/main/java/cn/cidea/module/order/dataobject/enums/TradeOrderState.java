package cn.cidea.module.order.dataobject.enums;

import lombok.AllArgsConstructor;

/**
 * @author: CIdea
 */
@AllArgsConstructor
public enum TradeOrderState {

    /**
     * 未支付
     */
    UNPAID,
    /**
     * 待配送
     */
    UNDELIVERED,
    /**
     * 待收件
     */
    WAIT_RECEIVE,
    /**
     * 已完成
     */
    COMPLETE,
    /**
     * 已关闭
     */
    CLOSED,

}
