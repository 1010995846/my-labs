package cn.cidea.module.admin.dataobject.entity.pool;

import java.util.Date;

import cn.cidea.module.admin.dataobject.entity.SysTenant;
import cn.cidea.module.admin.service.ISysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)Pool
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:25
 */
@Component
@Slf4j
public class SysTenantPool {

    @Autowired
    private ISysTenantService service;

    public Builder builder(SysTenant entity) {
        if (entity == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(Collections.singletonList(entity));
    }

    public Builder builder(Set<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysTenant> list = service.listByIds(ids);
        return new Builder(list);
    }

    public Builder builder(Collection<SysTenant> collection) {
        return new Builder(collection);
    }


    public class Builder {

        private final Collection<SysTenant> collection;

        public Builder(Collection<SysTenant> collection) {
            if (collection == null) {
                collection = Collections.emptyList();
            }
            this.collection = collection;
        }
    }
}

