package cn.cidea.server.service.system;

import cn.cidea.server.dataobject.entity.SysResource;
import cn.cidea.framework.common.utils.CollectionSteamUtils;
import cn.cidea.server.mybatis.CacheOneServiceImpl;
import cn.hutool.core.collection.CollUtil;
import cn.cidea.server.dal.mysql.ISysResourceMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * (SysResource)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Slf4j
@Service
public class SysResourceServiceImpl extends CacheOneServiceImpl<Long, ISysResourceMapper, SysResource> implements ISysResourceService {

    /**
     * 定时执行 {@link #schedulePeriodicRefresh()} 的周期
     * 因为已经通过 Redis Pub/Sub 机制，所以频率不需要高
     */
    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    /**
     * 权限与资源缓存
     * key：权限 {@link SysResource#getPermissions()}
     * value：SysResource 数组，因为一个权限可能对应多个资源
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Multimap<String, SysResource> permissionResourceCache;

    @Override
    protected void initLocalOtherCache(List<SysResource> list) {
        ImmutableMultimap.Builder<String, SysResource> permissionResourceCacheBuilder = ImmutableMultimap.builder();
        list.forEach(resource -> {
            for (String permission : resource.getPermissions()) {
                permissionResourceCacheBuilder.put(permission, resource);
            }
        });
        permissionResourceCache = permissionResourceCacheBuilder.build();
    }

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        initLocalCache();
    }

    @Override
    public Set<SysResource> listByPermissionFromCache(String... permissions){
        if(permissions == null){
            return new HashSet<>(0);
        }
        Set<SysResource> collect = Arrays.stream(permissions)
                .map(permission -> permissionResourceCache.get(permission))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return collect;
    }
}
