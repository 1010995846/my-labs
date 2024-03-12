package cn.cidea.module.pay.dataobject.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 支付订单(PayOrder)表实体类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:11
 */
@Data
public class PayOrderDTO implements Serializable {


    private Long id;
    /**
     * 支付APP。下单时确定
     */
    private Long appId;
    /**
     * 状态
     */
    private String status;
    /**
     * 完成时间
     */
    private Date successTime;
    /**
     * 失效时间
     */
    private Date expireTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date updateTime;

}

