package cn.cidea.server.mybatis;

import cn.cidea.server.dataobject.entity.SysRole;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

public abstract class CacheModel<T extends Model<T>> extends Model<T> {

    public abstract Date getUpdateTime();

}
