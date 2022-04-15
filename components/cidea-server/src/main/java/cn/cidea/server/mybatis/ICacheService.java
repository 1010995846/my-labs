package cn.cidea.server.mybatis;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ICacheService<P extends Serializable, T extends CacheModel<P, T>> extends IService<T> {

    /**
     * 初始化缓存
     */
    void initLocalCache();

    /**
     * 详情-缓存
     * @param id
     * @return
     */
    T getFromCache(P id);

    /**
     * 列表-缓存
     * @param ids
     * @return
     */
    List<T> listFromCache(Collection<P> ids);
}
