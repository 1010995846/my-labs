package cn.cidea.module.pay.dataobject.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付商户信息
 * @author: CIdea
 */
@Data
@Accessors(chain = true)
@TableName(value = "pay_merchant", autoResultMap = true)
public class PayMerchant extends Model<PayMerchant> {

    @TableId
    private Long id;
}
