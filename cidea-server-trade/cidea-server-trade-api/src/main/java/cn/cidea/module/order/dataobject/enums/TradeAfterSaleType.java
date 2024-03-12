package cn.cidea.module.order.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易售后 - 类型
 */
@Getter
@AllArgsConstructor
public enum TradeAfterSaleType {

    IN_SALE(10, "售中退款"), // 交易完成前买家申请退款
    AFTER_SALE(20, "售后退款"); // 交易完成后买家申请退款

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型名
     */
    private final String name;

}
