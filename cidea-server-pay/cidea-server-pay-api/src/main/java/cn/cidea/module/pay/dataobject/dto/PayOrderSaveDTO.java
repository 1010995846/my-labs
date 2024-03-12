package cn.cidea.module.pay.dataobject.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 支付订单(PayOrder)表实体类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:12
 */
@Data
public class PayOrderSaveDTO implements Serializable {

    /**
     * 支付APP。下单时确定
     */
    private Long appId;

    /**
     * 商户订单编号
     */
    @NotEmpty(message = "商户订单编号不能为空")
    private String merchantOrderId;
    /**
     * 商品标题
     */
    @NotEmpty(message = "商品标题不能为空")
    @Length(max = 32, message = "商品标题不能超过 32")
    private String subject;
    /**
     * 商品描述
     */
    @Length(max = 128, message = "商品描述信息长度不能超过128")
    private String body;

}

