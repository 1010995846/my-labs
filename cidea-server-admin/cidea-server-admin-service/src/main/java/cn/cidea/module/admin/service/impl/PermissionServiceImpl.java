package cn.cidea.module.admin.service.impl;

import cn.cidea.core.utils.CollSteamUtils;
import cn.cidea.framework.mybatisplus.plugin.cache.CacheServiceImpl;
import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.security.core.utils.SecurityFrameworkUtils;
import cn.cidea.module.admin.dal.mysql.ISysRoleResourceMapper;
import cn.cidea.module.admin.dal.mysql.ISysUserMapper;
import cn.cidea.module.admin.dataobject.entity.SysResource;
import cn.cidea.module.admin.dataobject.entity.SysRole;
import cn.cidea.module.admin.dataobject.entity.SysRoleResource;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import cn.cidea.module.admin.dataobject.entity.pool.SysRolePool;
import cn.cidea.module.admin.dataobject.entity.pool.SysUserPool;
import cn.cidea.module.admin.mq.producer.permission.PermissionProducer;
import cn.cidea.module.admin.service.IPermissionService;
import cn.cidea.module.admin.service.ISysResourceService;
import cn.cidea.module.admin.service.ISysRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Service("ps")
@Slf4j
public class PermissionServiceImpl extends CacheServiceImpl<ISysRoleResourceMapper, SysRoleResource> implements IPermissionService {

    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    /**
     * 角色编号与资源编号的缓存映射
     * key：角色编号
     * value：菜单编号的数组
     */
    private volatile Multimap<Long, Long> roleResourceCache;
    /**
     * 资源编号与角色编号的缓存映射
     * key：菜单编号
     * value：角色编号的数组
     */
    private volatile Multimap<Long, Long> resourceRoleCache;

    @Resource
    private ISysUserMapper userMapper;
    @Resource
    @Lazy
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

    @Override
    public void initLocalCache() {
        List<SysRoleResource> list = loadIfUpdate();
        ImmutableMultimap.Builder<Long, Long> roleResourceCacheBuilder = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<Long, Long> resourceRoleCacheBuilder = ImmutableMultimap.builder();
        list.forEach(re -> {
            roleResourceCacheBuilder.put(re.getRoleId(), re.getResourceId());
            resourceRoleCacheBuilder.put(re.getResourceId(), re.getRoleId());
        });
        roleResourceCache = roleResourceCacheBuilder.build();
        resourceRoleCache = resourceRoleCacheBuilder.build();
        maxUpdateTime = CollSteamUtils.getMaxValue(list, SysRoleResource::getUpdateTime);
        log.info("[initLocalCache][{}][初始化角色与资源的关联数量为 {}]", this.getClass().getSimpleName(), list.size());
    }

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        initLocalCache();
    }

    public Set<String> getPermission(SysUser user) {
        if (user == null) {
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
        userPool.builder(user)
                .roleBuilder()
                .resource();
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
        if (loginUser == null) {
            return false;
        }
        if(Boolean.TRUE.equals(loginUser.getSuperAdmin())){
            return true;
        }
        // 判断是否是超管。如果是，当然符合条件
        Set<Long> roleIds = loginUser.getRoleIds();
        if (roleIds == null) {
            return false;
        }
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }

        return Arrays.stream(permissions).anyMatch(permission -> {
            Set<SysResource> resourceList = resourceService.listByPermissionFromCache(permission);
            // 采用严格模式，如果权限找不到对应的 Resource 的话，认为
            if (CollUtil.isEmpty(resourceList)) {
                return false;
            }
            // 获得是否拥有该权限，任一一个
            return resourceList.stream().anyMatch(resource -> CollUtil.containsAny(roleIds, resourceRoleCache.get(resource.getId())));
        });
    }

    @Override
    public boolean superAdmin() {
        LoginUserDTO loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser == null || loginUser.getRoleIds() == null) {
            return false;
        }
        Set<Long> roleIds = loginUser.getRoleIds();
        return roleService.hasAnySuperAdmin(roleIds);
    }

    @Override
    public boolean hasRole(String code) {
        if (code == null) {
            return true;
        }
        LoginUserDTO loginUser = SecurityFrameworkUtils.getLoginUser();
        List<SysRole> roleList = roleService.listFromCache(loginUser.getRoleIds());
        return roleList.stream().anyMatch(role -> code.equals(role.getCode()));
    }

    @Override
    public boolean hasAnyRoles(String... codes) {
        if (codes == null) {
            return true;
        }
        LoginUserDTO loginUser = SecurityFrameworkUtils.getLoginUser();
        List<SysRole> roleList = roleService.listFromCache(loginUser.getRoleIds());
        List<String> codeList = roleList.stream()
                .map(SysRole::getCode)
                .collect(Collectors.toList());
        return Arrays.stream(codes).anyMatch(code -> codeList.contains(code));
    }

    @Override
    public void processRoleDeleted(Long roleId) {

        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        producer.sendRoleMenuRefreshMessage();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
            }

        });
    }

    @Override
    public void exception() {
        if(1 == 1){
            throw new RuntimeException("FFFFFFFFFFFFF");
        }
    }
}
