package cn.cidea.module.admin.dataobject.entity.pool;

import cn.cidea.module.admin.dataobject.entity.SysDept;
import cn.cidea.module.admin.service.ISysDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * (SysDepartment)Pool
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:27
 */
@Component
@Slf4j
public class SysDepartmentPool {

    @Autowired
    private ISysDepartmentService service;

    public Builder builder(SysDept entity) {
        if (entity == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(Collections.singletonList(entity));
    }

    public Builder builder(Set<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysDept> list = service.listByIds(ids);
        return new Builder(list);
    }

    public Builder builder(Collection<SysDept> collection) {
        return new Builder(collection);
    }


    public class Builder {

        private final Collection<SysDept> collection;

        public Builder(Collection<SysDept> collection) {
            if (collection == null) {
                collection = Collections.emptyList();
            }
            this.collection = collection;
        }
    }
}

