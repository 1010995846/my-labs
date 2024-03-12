package cn.cidea.module.pay.dataobject.factory;

import cn.cidea.core.utils.RelUtils;
import cn.cidea.module.pay.dal.mysql.IPayAppMapper;
import cn.cidea.module.pay.dal.mysql.IPayOrderExtensionMapper;
import cn.cidea.module.pay.dal.mysql.IPayOrderMapper;
import cn.cidea.module.pay.dataobject.entity.PayOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * 支付订单(PayOrder)表数据工厂类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:12
 */
@Component
public class PayOrderFactory {

    @Resource
    private IPayOrderMapper baseMapper;
    @Resource
    private IPayAppMapper appMapper;
    @Resource
    private IPayOrderExtensionMapper orderExtensionMapper;

    public Builder build(Serializable id) {
        PayOrder entity = baseMapper.selectById(id);
        Assert.notNull(entity, "id异常");
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(PayOrder entity) {
        if (entity == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(Set<Serializable> ids) {
        if (ids == null || ids.size() == 0) {
            return new Builder(new ArrayList<>(0));
        }
        Collection<PayOrder> coll = baseMapper.selectBatchIds(ids);
        return new Builder(coll);
    }

    public Builder build(Collection<PayOrder> coll) {
        if (coll == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(coll);
    }

    @Data
    @AllArgsConstructor
    public class Builder {

        private Collection<PayOrder> coll;

        public Builder app() {
            RelUtils.relOne(coll, PayOrder::getAppId, PayOrder::getApp, PayOrder::setApp, appMapper::selectBatchIds);
            return this;
        }

        public Builder extension() {
            // TODO CIdea:
            return this;
        }
    }

}

