package cn.cidea.server.service.system;

import cn.cidea.server.dataobject.entity.SysResource;
import cn.cidea.framework.common.utils.CollectionSteamUtils;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.cidea.server.dal.mysql.ISysResourceMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * (SysResource)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Slf4j
@Service
public class SysResourceServiceImpl extends ServiceImpl<ISysResourceMapper, SysResource> implements ISysResourceService {

    /**
     * 定时执行 {@link #schedulePeriodicRefresh()} 的周期
     * 因为已经通过 Redis Pub/Sub 机制，所以频率不需要高
     */
    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    /**
     * 菜单缓存
     * key：菜单编号
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Map<Long, SysResource> resourceCache;

    /**
     * 权限与菜单缓存
     * key：权限 {@link SysResource#getPermissions()}
     * value：MenuDO 数组，因为一个权限可能对应多个 MenuDO 对象
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Multimap<String, SysResource> permissionMenuCache;
    /**
     * 缓存菜单的最大更新时间，用于后续的增量轮询，判断是否有更新
     */
    private volatile Date maxUpdateTime;

    @Override
    @PostConstruct
    public synchronized void initLocalCache() {
        // 获取菜单列表，如果有更新
        List<SysResource> resourceList = this.loadMenuIfUpdate(maxUpdateTime);
        if (CollUtil.isEmpty(resourceList)) {
            return;
        }

        // 构建缓存
        ImmutableMap.Builder<Long, SysResource> menuCacheBuilder = ImmutableMap.builder();
        ImmutableMultimap.Builder<String, SysResource> permMenuCacheBuilder = ImmutableMultimap.builder();
        resourceList.forEach(resource -> {
            menuCacheBuilder.put(resource.getId(), resource);
            resource.getPermissions().forEach(permission -> permMenuCacheBuilder.put(permission, resource));
        });
        resourceCache = menuCacheBuilder.build();
        permissionMenuCache = permMenuCacheBuilder.build();
        maxUpdateTime = CollectionSteamUtils.getMaxValue(resourceList, SysResource::getUpdateTime);
        log.info("[initLocalCache][缓存菜单，数量为:{}]", resourceList.size());
    }

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        initLocalCache();
    }


    private List<SysResource> loadMenuIfUpdate(Date maxUpdateTime) {
        // 第一步，判断是否要更新。
        if (maxUpdateTime == null) { // 如果更新时间为空，说明 DB 一定有新数据
            log.info("[loadMenuIfUpdate][首次加载全量菜单]");
        } else { // 判断数据库中是否有更新的菜单
            if (baseMapper.selectExistsByUpdateTimeAfter(maxUpdateTime) == null) {
                return null;
            }
            log.info("[loadMenuIfUpdate][增量加载全量菜单]");
        }
        // 第二步，如果有更新，则从数据库加载所有菜单
        return baseMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<SysResource> listByPermissionFromCache(String permission) {
        return new ArrayList<>(permissionMenuCache.get(permission));
    }
}
