package cn.cidea.module.pay.dataobject.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 支付渠道
 * 一个应用下，会有多种支付渠道，例如说微信支付、支付宝支付等等
 */
@Data
@Accessors(chain = true)
public class PayChannelSaveDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 渠道编码
     */
    @NotBlank
    private String code;
    /**
     * 应用编号
     */
    @NotBlank
    private Long appId;

    @NotNull
    private JSONObject config;

    /**
     * 备注
     */
    private String remark;
}
