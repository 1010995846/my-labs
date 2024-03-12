package cn.cidea.module.order.dataobject.entity;

import cn.cidea.module.order.dataobject.enums.TradeOrderState;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: CIdea
 */
@Data
@Accessors(chain = true)
@TableName("trade_order")
public class Order extends Model<Order> {

    @TableId
    private String id;

    /**
     * {@link TradeOrderState}
     */
    private TradeOrderState status;

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户备注
     */
    private String userRemark;
    /**
     * 订单完成时间
     */
    private Date finishTime;

    private Date createTime;

    private Date updateTime;

}
