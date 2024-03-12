package cn.cidea.module.pay.dataobject.factory;

import cn.cidea.core.utils.RelUtils;
import cn.cidea.module.pay.dal.mysql.IPayAppMapper;
import cn.cidea.module.pay.dal.mysql.IPayChannelMapper;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
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
 * @author: CIdea
 */
@Component
public class PayAppFactory {

    @Resource
    private IPayAppMapper baseMapper;
    @Resource
    private IPayChannelMapper channelMapper;

    public Builder build(Serializable id) {
        PayApp entity = baseMapper.selectById(id);
        Assert.notNull(entity, "id异常");
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(PayApp entity) {
        if (entity == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(Set<Serializable> ids) {
        if (ids == null || ids.size() == 0) {
            return new Builder(new ArrayList<>(0));
        }
        Collection<PayApp> coll = baseMapper.selectBatchIds(ids);
        return new Builder(coll);
    }

    public Builder build(Collection<PayApp> coll) {
        if (coll == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(coll);
    }

    @Data
    @AllArgsConstructor
    public class Builder {

        private Collection<PayApp> coll;

        public Builder channel() {
            RelUtils.relSubMany(coll, PayChannel::getId, PayApp::getChannelList, PayApp::setChannelList, channelMapper::selectBatchIds);
            return this;
        }

    }

}
