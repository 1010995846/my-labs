package cn.cidea.framework.mybatisplus.plugin.cache;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public abstract class CacheModel<T extends Model<T>> extends Model<T> {

    public abstract Date getUpdateTime();

}
