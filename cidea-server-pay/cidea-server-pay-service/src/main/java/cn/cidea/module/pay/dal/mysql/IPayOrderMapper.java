package cn.cidea.module.pay.dal.mysql;


import cn.cidea.framework.web.core.asserts.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.cidea.module.pay.dataobject.entity.PayOrder;

/**
 * 支付订单(PayOrder)表数据库访问层
 *
 * @author CIdea
 * @since 2023-11-30 09:53:11
 */
public interface IPayOrderMapper extends BaseMapper<PayOrder> {

    default PayOrder selectByAppIdAndMerchantOrderId(Long appId, String merchantOrderId) {
        return selectOne(new QueryWrapper<PayOrder>().lambda()
                .eq(PayOrder::getAppId, appId)
                .eq(PayOrder::getMerchantOrderId, merchantOrderId));
    }

    default PayOrder validById(Long id){
        PayOrder order = selectById(id);
        Assert.VALID.nonNull(order, "支付订单ID非法");
        return order;
    }
}

