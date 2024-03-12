package cn.cidea.module.pay.dataobject.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 支付订单(PayOrder)表实体类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:12
 */
@Data
public class PayOrderUpdateDTO implements Serializable {


    private Long id;

    private String channelCode;
    private String returnUrl;

}

