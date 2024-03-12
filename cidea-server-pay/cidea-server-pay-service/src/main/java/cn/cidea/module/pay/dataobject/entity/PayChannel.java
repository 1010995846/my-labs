package cn.cidea.module.pay.dataobject.entity;

import cn.cidea.module.pay.client.PayClientConfig;
import cn.cidea.module.pay.dataobject.enums.PayChannelEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付渠道
 * 一个应用下，会有多种支付渠道，例如说微信支付、支付宝支付等等
 */
@Data
@Accessors(chain = true)
@TableName(value = "pay_channel", autoResultMap = true)
public class PayChannel extends Model<PayChannel> {

    /**
     * 渠道编号，数据库自增
     */
    @TableId
    private Long id;
    /**
     * 渠道编码
     *
     * 枚举 {@link PayChannelEnum}
     */
    private String code;
    /**
     * 渠道费率，单位：百分比
     */
    private Double feeRate;
    /**
     * 备注
     */
    private String remark;

    /**
     * 商户编号
     *
     * 关联 {@link PayMerchant#id}
     */
    private Long merchantId;
    /**
     * 应用编号
     *
     * 关联 {@link PayAppDO#getId()}
     */
    private Long appId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 支付渠道配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PayClientConfig config;

}
