package cn.cidea.module.admin.dataobject.entity.pool;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.ISysResourceMapper;
import cn.cidea.module.admin.dal.mysql.ISysRoleResourceMapper;
import cn.cidea.module.admin.dataobject.entity.SysResource;
import cn.cidea.module.admin.dataobject.entity.SysRole;
import cn.cidea.module.admin.dataobject.entity.SysRoleResource;
import cn.cidea.module.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Lazy
@Slf4j
@Component
public class SysRolePool {
    
    @Autowired
    private ISysRoleService service;
    @Autowired
    private ISysRoleResourceMapper roleResourceMapper;
    @Autowired
    private ISysResourceMapper resourceMapper;
    
    public Builder builder(SysRole user) {
        if (user == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(Collections.singletonList(user));
    }

    public Builder builder(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysRole> list = service.listByIds(ids);
        return new Builder(list);
    }

    public Builder builder(Collection<SysRole> collection) {
        if (collection == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(collection);
    }

    public Builder builder(Long id) {
        SysRole user = service.getById(id);
        Assert.BAD_REQUEST.nonNull(user);
        return builder(user);
    }

    public Builder builderFromCache(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysRole> list = service.listFromCache(ids);
        return new Builder(list);
    }

    public class Builder {

        @Getter
        private Collection<SysRole> collection;

        public Builder(Collection<SysRole> collection) {
            this.collection = collection;
        }
        
        public Builder resource(){
            Map<Long, SysRole> map = collection.stream()
                    .filter(u -> u.getResources() == null)
                    .collect(Collectors.toMap(SysRole::getId, u -> u));
            if(map.isEmpty()){
                return this;
            }
            List<SysRoleResource> relList = roleResourceMapper.selectList(new QueryWrapper<SysRoleResource>().lambda()
                    .in(SysRoleResource::getRoleId, map.keySet()));
            Set<Long> targetIdSet = relList.stream().map(SysRoleResource::getResourceId).collect(Collectors.toSet());
            Map<Long, SysResource> targetMap = resourceMapper.selectList(new QueryWrapper<SysResource>().lambda()
                            .in(SysResource::getId, targetIdSet))
                    .stream()
                    .collect(Collectors.toMap(SysResource::getId, r -> r));

            Map<Long, List<SysRoleResource>> relMap = relList.stream()
                    .collect(Collectors.groupingBy(SysRoleResource::getRoleId));
            for (SysRole main : map.values()) {
                List<SysRoleResource> singleRelList = relMap.getOrDefault(main.getId(), Collections.emptyList());
                List<SysResource> singleTargetList = singleRelList.stream().map(roleId -> {
                    SysResource target = targetMap.get(roleId);
                    if (target == null) {
                        log.warn("引用ID异常!");
                    }
                    return target;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                main.setResources(singleTargetList);
            }
            return this;
        }
    }
}
