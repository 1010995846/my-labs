package cn.cidea.module.pay.dataobject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 支付渠道
 * 一个应用下，会有多种支付渠道，例如说微信支付、支付宝支付等等
 */
@Data
@Accessors(chain = true)
public class PayChannelDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

}
