package cn.cidea.framework.mybatisplus.plugin.cache;

import cn.cidea.framework.common.utils.CollectionSteamUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [mybatisPlusService + 单表缓存]实现类
 * @author Charlotte
 */
@Slf4j
public abstract class CacheOneServiceImpl<P extends Serializable, M extends BaseMapper<T>, T extends CacheOneModel<P, T>> extends CacheServiceImpl<M, T>  {

    /**
     * 角色缓存
     * key：角色编号 {@link Model#pkVal()} ()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    protected volatile Map<P, T> cache;

    /**
     * 初始化 {@link #cache} 缓存
     */
    @Override
    public void initLocalCache() {
        // 获取角色列表，如果有更新
        List<T> list = loadIfUpdate();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 写入缓存
        cache = CollectionSteamUtils.convertMap(list, CacheOneModel::pkVal);
        initLocalOtherCache(list);
        maxUpdateTime = CollectionSteamUtils.getMaxValue(list, CacheModel::getUpdateTime);
        log.info("[initLocalCache][{}][初始化数量为 {}]", this.getClass().getSimpleName(), list.size());
    }

    /**
     * 初始化其它映射关系的缓存
     * @param list
     */
    protected void initLocalOtherCache(List<T> list){}

    public T getFromCache(P id) {
        return cache.get(id);
    }

    public List<T> listFromCache(Collection<P> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return cache.values().stream().filter(e -> ids.contains(e.pkVal()))
                .collect(Collectors.toList());
    }
}
