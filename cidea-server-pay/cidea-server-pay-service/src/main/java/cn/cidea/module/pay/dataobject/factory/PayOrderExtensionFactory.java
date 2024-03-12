package cn.cidea.module.pay.dataobject.factory;

import cn.cidea.module.pay.dal.mysql.IPayOrderExtensionMapper;
import cn.cidea.module.pay.dataobject.entity.PayOrderExtension;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * order每次发起支付的记录和凭证(PayOrderExtension)表数据工厂类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:14
 */
@Component
public class PayOrderExtensionFactory {

    @Autowired
    private IPayOrderExtensionMapper baseMapper;

    public Builder build(Serializable id) {
        PayOrderExtension entity = baseMapper.selectById(id);
        Assert.notNull(entity, "id异常");
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(PayOrderExtension entity) {
        if (entity == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(Set<Serializable> ids) {
        if (ids == null || ids.size() == 0) {
            return new Builder(new ArrayList<>(0));
        }
        Collection<PayOrderExtension> coll = baseMapper.selectBatchIds(ids);
        return new Builder(coll);
    }

    public Builder build(Collection<PayOrderExtension> coll) {
        if (coll == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(coll);
    }

    @Data
    @AllArgsConstructor
    public class Builder {

        private Collection<PayOrderExtension> coll;

    }

}

