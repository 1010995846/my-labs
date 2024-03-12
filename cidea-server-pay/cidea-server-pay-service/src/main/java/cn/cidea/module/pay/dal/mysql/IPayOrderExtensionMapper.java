package cn.cidea.module.pay.dal.mysql;


import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.cidea.module.pay.dataobject.entity.PayOrderExtension;

/**
 * order每次发起支付的记录和凭证(PayOrderExtension)表数据库访问层
 *
 * @author CIdea
 * @since 2023-11-30 09:53:13
 */
public interface IPayOrderExtensionMapper extends BaseMapper<PayOrderExtension> {

    default List<PayOrderExtension> selectListByOrderId(Long orderId){
        return selectList(new QueryWrapper<PayOrderExtension>().lambda().eq(PayOrderExtension::getOrderId, orderId));
    }
}

