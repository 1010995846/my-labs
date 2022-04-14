package cn.cidea.server.service.system;

import cn.cidea.server.dal.mysql.ISysUserMapper;
import cn.cidea.server.dal.mysql.ISysUserRoleMapper;
import cn.cidea.server.dataobject.dto.LoginUserDTO;
import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.server.framework.security.utils.SecurityFrameworkUtils;
import cn.cidea.server.mq.producer.permission.PermissionProducer;
import cn.hutool.core.util.ArrayUtil;
import cn.cidea.server.dal.mysql.ISysRoleResourceMapper;
import cn.cidea.server.dataobject.entity.pool.SysRolePool;
import cn.cidea.server.dataobject.entity.pool.SysUserPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Service("ps")
@Slf4j
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Resource
    private ISysUserMapper userMapper;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysRoleResourceMapper roleResourceMapper;
    @Resource
    private ISysResourceService resourceService;

    @Resource
    @Lazy
    private SysUserPool userPool;
    @Resource
    @Lazy
    private SysRolePool rolePool;

    @Resource
    private PermissionProducer producer;

    public Set<String> getPermission(SysUser user) {
        if(user == null){
            return new HashSet<>(0);
        }
        // Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        // if (user.isAdmin()) {
        //     roles.add("*:*:*"); // 所有模块
        // } else {
            // 读取
            // roles.addAll(resourceService.selectMenuPermsByUserId(user.getId()));
        // }
        userPool.builder(user).roleBuilder().resource();
        Set<String> permSet = user.getRoles().stream()
                .flatMap(r -> r.getResources().stream())
                .flatMap(r -> r.getPermissions().stream())
                .collect(Collectors.toSet());
        return permSet;
    }

    public Set<String> getPermission(Long userId) {
        return getPermission(userMapper.selectById(userId));
    }

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    public boolean hasAnyPermissions(String... permissions) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        LoginUserDTO loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null || loginUser.getRoleIds() == null){
            return false;
        }
        // 判断是否是超管。如果是，当然符合条件
        Set<Long> roleIds = loginUser.getRoleIds();
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }

        // 遍历权限，判断是否有一个满足
        // TODO 角色缓存、资源缓存
        Set<String> permSet = rolePool.builderFromCache(roleIds)
                .resource()
                .getCollection()
                .stream()
                .flatMap(r -> r.getResources().stream())
                .flatMap(r -> r.getPermissions().stream())
                .collect(Collectors.toSet());
        return Arrays.stream(permissions).anyMatch(permission -> permSet.contains(permission));
        // return Arrays.stream(permissions).anyMatch(permission -> {
        //     List<SysResource> resourceList = resourceService.listByPermissionFromCache(permission);
        //     // 采用严格模式，如果权限找不到对应的 Resource 的话，认为
        //     if (CollUtil.isEmpty(resourceList)) {
        //         return false;
        //     }
        //     // 获得是否拥有该权限，任一一个
        //     return resourceList.stream().anyMatch(resource -> CollUtil.containsAny(roleIds, menuRoleCache.get(resource.getId())));
        // });
    }

    @Override
    public boolean hasRole(String role) {
        return false;
    }

    @Override
    public boolean hasAnyRoles(String... roles) {
        return false;
    }

    @Override
    public void processRoleDeleted(Long roleId) {

        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                producer.sendRoleMenuRefreshMessage();
            }

        });
    }
}
