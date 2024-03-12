package cn.cidea.module.order.dataobject.entity;

import cn.cidea.module.order.dataobject.enums.TradeAfterSaleStatus;
import cn.cidea.module.order.dataobject.enums.TradeAfterSaleType;
import cn.cidea.module.order.dataobject.enums.TradeAfterSaleWay;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易售后，用于处理 {@link Order} 交易订单的退款退货流程
 */
@Data
@Accessors(chain = true)
@TableName(value = "trade_after_sale", autoResultMap = true)
public class AfterSale extends Model<AfterSale> {

    /**
     *
     */
    @TableId
    private String id;
    /**
     * 退款状态
     *
     * 枚举 {@link TradeAfterSaleStatus}
     */
    private Integer status;
    /**
     * 售后方式
     *
     * 枚举 {@link TradeAfterSaleWay}
     */
    private Integer way;
    /**
     * 售后类型
     *
     * 枚举 {@link TradeAfterSaleType}
     */
    private Integer type;
    /**
     * 申请原因
     *
     * type = 退款，对应 trade_after_sale_refund_reason 类型
     * type = 退货退款，对应 trade_after_sale_refund_and_return_reason 类型
     */
    private String applyReason;
    /**
     * 补充描述
     */
    private String applyDescription;
    /**
     * 补充凭证图片
     *
     * 数组，以逗号分隔
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> applyPicUrls;

    // ========== 交易订单相关 ==========
    /**
     * 交易订单编号
     *
     * {@link Order#getId()}
     */
    private Long orderId;

    // ========== 审批相关 ==========

    /**
     * 审批时间
     */
    private LocalDateTime auditTime;
    /**
     * 审批人
     *
     * 关联 AdminUserDO 的 id 编号
     */
    private Long auditUserId;
    /**
     * 审批备注
     *
     * 注意，只有审批不通过才会填写
     */
    private String auditReason;

    // ========== 退款相关 ==========
    /**
     * 退款金额，单位：分。
     */
    private Integer refundPrice;
    /**
     * 支付退款编号
     *
     * 对接 pay-module-biz 支付服务的退款订单编号，即 PayRefundDO 的 id 编号
     */
    private Long payRefundId;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    // ========== 退货相关 ==========
    /**
     * 退货物流公司编号
     *
     * 关联 LogisticsDO 的 id 编号
     */
    private Long logisticsId;
    /**
     * 退货物流单号
     */
    private String logisticsNo;
    /**
     * 退货时间
     */
    private LocalDateTime deliveryTime;
    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;
    /**
     * 收货备注
     *
     * 注意，只有拒绝收货才会填写
     */
    private String receiveReason;

}
