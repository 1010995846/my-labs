package cn.cidea.module.pay.dataobject.entity;

import cn.cidea.core.utils.function.IPK;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 支付应用，一个商户有多个支付应用
 * 公众号、小程序
 * @author: CIdea
 */
@Data
@Accessors(chain = true)
@TableName(value = "pay_app", autoResultMap = true)
public class PayApp extends Model<PayApp> implements IPK {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 应用名
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 支付结果的回调地址
     */
    private String orderNotifyUrl;
    /**
     * 退款结果的回调地址
     */
    private String refundNotifyUrl;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 渠道列表
     */
    @TableField(exist = false)
    private List<PayChannel> channelList;
}
