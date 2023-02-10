package cn.cidea.framework.mybatisplus.plugin.cache;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * @author Charlotte
 */
public abstract class CacheOneModel<P extends Serializable, T extends Model<T>> extends CacheModel<T> {

    @Override
    public P pkVal() {
        return (P) super.pkVal();
    }

}
