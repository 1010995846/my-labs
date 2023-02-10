package cn.cidea.module.pm.dataobject.factory;

import cn.cidea.module.pm.dal.mysql.IPmPropMapper;
import cn.cidea.module.pm.dataobject.dto.PmProductValDTO;
import cn.cidea.module.pm.dataobject.dto.PmPropDTO;
import cn.cidea.module.pm.dataobject.entity.PmProduct;
import cn.cidea.module.pm.dal.mysql.IPmProductMapper;
import cn.cidea.module.pm.dataobject.convert.PmPropConvert;
import cn.cidea.module.pm.dataobject.entity.PmProp;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 产品(PmProduct)表数据工厂类
 *
 * @author CIdea
 * @since 2023-01-10 09:50:06
 */
@Component
public class PmProductFactory {

    @Autowired
    private IPmProductMapper baseMapper;
    @Autowired
    private IPmPropMapper propMapper;

    public Builder build(Serializable id) {
        PmProduct entity = baseMapper.selectById(id);
        Assert.notNull(entity, "id异常");
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(PmProduct entity) {
        if (entity == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(Collections.singleton(entity));
    }

    public Builder build(Set<Serializable> ids) {
        if (ids == null || ids.size() == 0) {
            return new Builder(new ArrayList<>(0));
        }
        Collection<PmProduct> coll = baseMapper.selectBatchIds(ids);
        return new Builder(coll);
    }

    public Builder build(Collection<PmProduct> coll) {
        if (coll == null) {
            return new Builder(new ArrayList<>(0));
        }
        return new Builder(coll);
    }

    @Data
    @AllArgsConstructor
    public class Builder {

        private Collection<PmProduct> coll;

        public Builder prop(){
            Set<String> propIds = coll.stream()
                    .filter(e -> e.getProperties() == null)
                    .map(PmProduct::getPropVal)
                    .filter(Objects::isNull)
                    .flatMap(e -> e.keySet().stream()).collect(Collectors.toSet());
            if(CollectionUtils.isEmpty(propIds)){
                return this;
            }
            Map<String, PmPropDTO> propMap = propMapper.selectBatchIds(propIds).stream()
                    .collect(Collectors.toMap(PmProp::getId, PmPropConvert.INSTANCE::toDTO));
            coll.parallelStream().forEach(e -> {
                Map<String, PmProductValDTO> properties = new ConcurrentHashMap<>();
                e.setProperties(properties);
                e.getPropVal().entrySet().stream().forEach(entry -> {
                    PmPropDTO prop = propMap.get(entry.getKey());
                    if(prop == null){
                        return;
                    }
                    PmProductValDTO val = new PmProductValDTO();
                    val.setProp(prop);
                    val.setVal(entry.getValue());
                    properties.put(entry.getKey(), val);
                });
                e.setProperties(e.getPropVal().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    PmProductValDTO val = new PmProductValDTO();
                    val.setProp(propMap.get(entry.getKey()));
                    val.setVal(entry.getValue());
                    return val;
                })));
            });
            return this;
        }

    }

}

