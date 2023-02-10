package cn.cidea.module.admin.dataobject.entity.pool;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.*;
import cn.cidea.module.admin.dataobject.entity.SysRole;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import cn.cidea.module.admin.dataobject.entity.SysUserRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Lazy
@Slf4j
@Component
public class SysUserPool {

    @Autowired
    private ISysUserMapper baseMapper;
    @Autowired
    private ISysUserRoleMapper userRoleMapper;
    @Autowired
    private ISysRoleMapper roleMapper;
    @Autowired
    private ISysUserDepartmentRelMapper departmentRelMapper;
    @Autowired
    private ISysDepartmentMapper departmentMapper;

    public Builder builder(SysUser user) {
        if (user == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(Collections.singletonList(user));
    }

    public Builder builder(Set<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return new Builder(Collections.emptyList());
        }
        List<SysUser> list = baseMapper.selectBatchIds(ids);
        return new Builder(list);
    }

    public Builder builder(Collection<SysUser> collection) {
        if (collection == null) {
            return new Builder(Collections.emptyList());
        }
        return new Builder(collection);
    }

    public Builder builder(Long userId) {
        SysUser user = baseMapper.selectById(userId);
        Assert.BAD_REQUEST.nonNull(user);
        return builder(user);
    }

    public class Builder {

        private Collection<SysUser> collection;

        public Builder(Collection<SysUser> collection) {
            this.collection = collection;
        }

        public Builder role(){
            Map<Long, SysUser> map = collection.stream()
                    .filter(u -> u.getRoles() == null)
                    .collect(Collectors.toMap(SysUser::getId, u -> u));
            if(map.isEmpty()){
                return this;
            }
            List<SysUserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<SysUserRole>().lambda()
                    .in(SysUserRole::getUserId, map.keySet()));
            Set<Long> roleIdSet = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
            Map<Long, SysRole> roleMap;
            if(roleIdSet.size() != 0){
                roleMap = roleMapper.selectList(new QueryWrapper<SysRole>().lambda()
                                .in(SysRole::getId, roleIdSet))
                        .stream()
                        .collect(Collectors.toMap(SysRole::getId, r -> r));
            } else {
                roleMap = Collections.emptyMap();
            }

            Map<Long, List<SysUserRole>> userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(SysUserRole::getUserId));
            for (SysUser user : map.values()) {
                List<SysUserRole> singleUserRoleList = userRoleMap.getOrDefault(user.getId(), Collections.emptyList());
                List<SysRole> roleList = singleUserRoleList.stream().map(rel -> {
                    SysRole role = roleMap.get(rel.getRoleId());
                    if (role == null) {
                        log.warn("SysUserRole.roleId异常!");
                    }
                    return role;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                user.setRoles(roleList);
            }
            return this;
        }

        public SysRolePool.Builder roleBuilder(){
            role();
            List<SysRole> roleList = collection.stream().flatMap(u -> u.getRoles().stream()).collect(Collectors.toList());
            return new SysRolePool().builder(roleList);
        }
    }


}
