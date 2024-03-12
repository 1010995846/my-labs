package cn.cidea.module.pay.dataobject.entity;


import cn.cidea.module.pay.dataobject.enums.PayOrderStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 支付订单(PayOrder)表实体类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:09
 */
@Data
public class PayOrder extends Model<PayOrder> implements Serializable {


    @TableId
    private Long id;

    /**
     * 支付APP。下单时确定
     */
    private Long appId;
    @TableField(exist = false)
    private PayApp app;

    private BigDecimal amt;

    /**
     * 状态
     * {@link PayOrderStatusEnum}
     */
    private PayOrderStatusEnum status;

    private Long extensionId;

    // ========== 发起支付商户传入字段 ==========

    /**
     * 商户订单编号
     */
    private String merchantOrderId;
    /**
     * 商品标题
     */
    private String subject;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 完成时间
     */
    private LocalDateTime successTime;
    /**
     * 失效时间
     */
    private LocalDateTime expireTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}

