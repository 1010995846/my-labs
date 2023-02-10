package cn.cidea.framework.mybatisplus.plugin.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlotte
 */
public interface ICacheOneService<P extends Serializable, T> extends ICacheService {

    /**
     * 详情-缓存
     *
     * @param id
     * @return
     */
    T getFromCache(P id);

    /**
     * 列表-缓存
     *
     * @param ids
     * @return
     */
    List<T> listFromCache(Collection<P> ids);
}
