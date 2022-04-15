package cn.cidea.server.mybatis;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlotte
 */
public interface ICacheService {

    /**
     * 初始化缓存
     */
    void initLocalCache();
}
