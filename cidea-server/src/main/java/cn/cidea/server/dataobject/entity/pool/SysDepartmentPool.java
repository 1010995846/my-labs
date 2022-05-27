package cn.cidea.server.dataobject.entity.pool;

import java.util.Date;

import cn.cidea.server.service.ISysDepartmentService;
import cn.cidea.server.dataobject.entity.SysDepartment;
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

    public Builder builder(SysDepartment entity) {
        if (entity == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(Collections.singletonList(entity));
    }

    public Builder builder(Set<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysDepartment> list = service.listByIds(ids);
        return new Builder(list);
    }

    public Builder builder(Collection<SysDepartment> collection) {
        return new Builder(collection);
    }


    public class Builder {

        private final Collection<SysDepartment> collection;

        public Builder(Collection<SysDepartment> collection) {
            if (collection == null) {
                collection = Collections.emptyList();
            }
            this.collection = collection;
        }
    }
}

