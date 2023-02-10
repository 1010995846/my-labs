package cn.cidea.module.pm.dataobject.factory;

import cn.cidea.module.pm.dal.mysql.IPmMfrMapper;
import cn.cidea.module.pm.dataobject.entity.PmMfr;
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
 * 产商(PmMfr)表数据工厂类
 *
 * @author CIdea
 * @since 2023-01-10 11:11:59
 */
@Component
public class PmMfrFactory {

    @Autowired
    private IPmMfrMapper baseMapper;

    public Builder build(Serializable id) {
        PmMfr entity = baseMapper.selectById(id);
        Assert.notNull(entity, "id异常");
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(PmMfr entity) {
        if (entity == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(Set<Serializable> ids) {
        if (ids == null || ids.size() == 0) {
            return new Builder(new ArrayList<>(0));
        }
        Collection<PmMfr> coll = baseMapper.selectBatchIds(ids);
        return new Builder(coll);
    }

    public Builder build(Collection<PmMfr> coll) {
        if (coll == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(coll);
    }

    @Data
    @AllArgsConstructor
    public class Builder {

        private Collection<PmMfr> coll;

    }

}

