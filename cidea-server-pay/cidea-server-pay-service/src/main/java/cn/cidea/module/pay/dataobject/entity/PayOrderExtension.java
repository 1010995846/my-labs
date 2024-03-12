package cn.cidea.module.pay.dataobject.entity;


import cn.cidea.module.pay.dataobject.enums.PayOrderStatusEnum;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * order每次发起支付的记录和凭证(PayOrderExtension)表实体类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:13
 */
@Data
@Accessors(chain = true)
public class PayOrderExtension extends Model<PayOrderExtension> implements Serializable {


    @TableId
    private Long id;

    /**
     * 支付订单ID
     */
    private Long orderId;

    /**
     * 状态
     * {@link PayOrderStatusEnum}
     */
    private PayOrderStatusEnum status;

    /**
     * 是否有效
     */
    private Boolean enabled;
    /**
     * 选择的支付渠道ID
     */
    private Long channelId;
    /**
     * 选择的支付渠道信息【快照】
     */
    private String channelSnapshot;
    /**
     * 对接支付系统传入的编号
     */
    private String no;
    /**
     * 对接支付系统内部的单号
     */
    private String extId;
    /**
     * 对接支付系统需要的参数
     */
    private String extras;
    private String channelNotifyData;
    /**
     * 调用支付系统返回的信息
     */
    private String rspMsg;
    /**
     * 调用支付系统回调的信息
     */
    private String notifyData;
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
    private Boolean deleted;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}

