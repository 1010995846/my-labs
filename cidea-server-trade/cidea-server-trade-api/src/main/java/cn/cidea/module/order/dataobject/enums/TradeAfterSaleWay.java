package cn.cidea.module.order.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易售后 - 方式
 */
@Getter
@AllArgsConstructor
public enum TradeAfterSaleWay {

    REFUND(10, "仅退款"),
    RETURN_AND_REFUND(20, "退货退款");

    /**
     * 方式
     */
    private final Integer way;
    /**
     * 方式名
     */
    private final String name;

}
