package cn.cidea.module.pay.dataobject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付应用，一个商户有多个支付应用
 * 公众号、小程序
 * @author: CIdea
 */
@Data
@Accessors(chain = true)
public class PayAppDTO implements Serializable {

    /**
     * ID
     */
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
     * 渠道列表
     */
    private List<PayChannelDTO> channelList;

}
