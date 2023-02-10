package cn.cidea.module.admin.dal.redis;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @author Charlotte
 */
public interface ICacheDAO<E extends Model<E>> {

    void insert(E entity) throws InterruptedException;

    void deleteById(Serializable id);

    void deleteBatchIds(Collection<Serializable> ids);

    E selectById(Serializable id);

    Collection<E> selectBatchIds(Set<Serializable> ids);
}
