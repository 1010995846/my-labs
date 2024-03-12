package cn.cidea.module.pay.dataobject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 支付商户信息
 * @author: CIdea
 */
@Data
@Accessors(chain = true)
public class PayMerchantSaveDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

}
