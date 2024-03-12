package cn.cidea.module.pay.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 渠道的退款状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum {

    /**
     * 未退款
     */
    WAITING,
    /**
     * 退款成功
     */
    SUCCESS,
    /**
     * 退款失败
     */
    FAILURE;

    public static boolean isSuccess(String val) {
        return Objects.equals(val, SUCCESS.name());
    }
    public static boolean isSuccess(PayRefundStatusEnum status) {
        return isSuccess(status.name());
    }

    public static boolean isFailure(String val) {
        return Objects.equals(val, FAILURE.name());
    }
    public static boolean isFailure(PayRefundStatusEnum status) {
        return isFailure(status.name());
    }

}
