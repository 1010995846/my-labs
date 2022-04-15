package cn.cidea.server.mybatis;

import cn.cidea.framework.common.utils.CollectionSteamUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Slf4j
public class CacheServiceImpl<P extends Serializable, M extends BaseMapper<T>, T extends CacheModel<P, T>> extends ServiceImpl<M, T> implements ICacheService<P, T> {

    /**
     * 角色缓存
     * key：角色编号 {@link Model#pkVal()} ()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    protected volatile Map<P, T> cache;
    /**
     * 缓存角色的最大更新时间，用于后续的增量轮询，判断是否有更新
     */
    @Getter
    private volatile Date maxUpdateTime;

    /**
     * 初始化 {@link #cache} 缓存
     */
    @PostConstruct
    @Override
    public void initLocalCache() {
        // 获取角色列表，如果有更新
        List<T> list = loadIfUpdate(maxUpdateTime);
        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 写入缓存
        cache = CollectionSteamUtils.convertMap(list, CacheModel::pkVal);
        maxUpdateTime = CollectionSteamUtils.getMaxValue(list, CacheModel::getUpdateTime);
        log.info("[initLocalCache][初始化 Role 数量为 {}]", list.size());
    }

    protected List<T> loadIfUpdate(Date maxUpdateTime) {
        // 第一步，判断是否要更新。
        if (maxUpdateTime == null) {
            // 如果更新时间为空，说明 DB 一定有新数据
            log.info("[loadRoleIfUpdate][首次加载全量数据]");
        } else {
            // 判断数据库中是否有更新的角色
            if (count(new QueryWrapper<T>()
                    .gt("update_time", maxUpdateTime)) == 0) {
                return null;
            }
            log.info("[loadRoleIfUpdate][增量加载全量数据]");
        }
        // 第二步，如果有更新，则从数据库加载所有角色
        return list();
    }

    @Override
    public T getFromCache(P id) {
        return cache.get(id);
    }

    @Override
    public List<T> listFromCache(Collection<P> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return cache.values().stream().filter(e -> ids.contains(e.pkVal()))
                .collect(Collectors.toList());
    }
}
