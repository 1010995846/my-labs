package cn.cidea.module.order.dataobject.enums;

import lombok.AllArgsConstructor;

/**
 * @author: CIdea
 */
@AllArgsConstructor
public enum OrderStateEvent {

    /**
     * 已支付
     */
    PAYED,
    /**
     * 开始配送
     */
    DELIVERY,
    /**
     * 收件
     */
    RECEIVED,
    /**
     * 取消
     */
    CANCEL,
    /**
     * 退款
     */
    REFUND,
    /**
     * 过期
     */
    EXPIRE,
    ;


}
