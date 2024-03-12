package cn.cidea.module.pay.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author: CIdea
 */
@Getter
@AllArgsConstructor
public enum PayOrderStatusEnum {

    /**
     * 未支付
     */
    WAITING,
    /**
     * 支付成功
     */
    SUCCESS,
    /**
     * 已退款
     */
    REFUND,
    /**
     * 支付关闭
     * 注意：全部退款后，还是 REFUND 状态
     */
    CLOSED,
    ;

    /**
     * 判断是否支付成功
     *
     * @param val 状态
     * @return 是否支付成功
     */
    public static boolean isSuccess(String val) {
        return Objects.equals(val, SUCCESS.name());
    }

    public static boolean isSuccess(PayOrderStatusEnum status) {
        return isSuccess(status.name());
    }

    /**
     * 判断是否支付成功或者已退款
     *
     * @param val 状态
     * @return 是否支付成功或者已退款
     */
    public static boolean isSuccessOrRefund(String val) {
        return val.equals(SUCCESS.name()) || val.equals(REFUND.name());
    }
    public static boolean isSuccessOrRefund(PayOrderStatusEnum status) {
        return isSuccessOrRefund(status.name());
    }

    /**
     * 判断是否支付关闭
     *
     * @param val 状态
     * @return 是否支付关闭
     */
    public static boolean isClosed(String val) {
        return Objects.equals(val, CLOSED.name());
    }
    public static boolean isClosed(PayOrderStatusEnum status) {
        return isClosed(status.name());
    }
}
