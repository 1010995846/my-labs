package cn.cidea.server.service.system;


import cn.cidea.server.dataobject.entity.SysResource;
import cn.cidea.framework.mybatisplus.plugin.cache.ICacheOneService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * (SysResource)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Transactional(readOnly = true)
public interface ISysResourceService extends ICacheOneService<Long, SysResource> {

    Set<SysResource> listByPermissionFromCache(String... permissions);
}
