package cn.cidea.module.admin.dataobject.entity.pool;


import cn.cidea.module.admin.dataobject.entity.SysMessage;
import cn.cidea.module.admin.service.system.ISysMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * (SysMessage)Pool
 *
 * @author yechangfei
 * @since 2022-04-27 14:11:20
 */
@Component
@Slf4j
public class SysMessagePool {

    @Autowired
    private ISysMessageService service;

    public Builder builder(SysMessage entity) {
        if (entity == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(Collections.singletonList(entity));
    }

    public Builder builder(Set<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysMessage> list = service.listByIds(ids);
        return new Builder(list);
    }

    public Builder builder(Collection<SysMessage> collection) {
        return new Builder(collection);
    }


    public class Builder {

        private final Collection<SysMessage> collection;

        public Builder(Collection<SysMessage> collection) {
            if (collection == null) {
                collection = Collections.emptyList();
            }
            this.collection = collection;
        }
    }
}

