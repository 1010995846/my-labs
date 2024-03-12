package cn.cidea.module.pay.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一下单 Response DTO
 */
@Data
@Accessors(chain = true)
public class PayOrderUnifiedRespDTO {

    /**
     * 展示模式
     */
    private String displayMode;
    /**
     * 展示内容
     */
    private String displayContent;

}
