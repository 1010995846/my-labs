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
public abstract class CacheServiceImpl<M extends BaseMapper<T>, T extends CacheModel<T>> extends ServiceImpl<M, T> implements ICacheService {

    /**
     * 缓存的最后更新时间，用于后续的增量轮询，判断是否有更新
     */
    @Getter
    protected volatile Date maxUpdateTime;

    /**
     * 初始化缓存
     */
    @Override
    @PostConstruct
    public abstract void initLocalCache();

    /**
     * @return
     */
    protected List<T> loadIfUpdate() {
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

}
