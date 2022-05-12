package cn.cidea.server.service.system;

import cn.cidea.framework.mybatisplus.plugin.cache.ICacheService;
import cn.cidea.server.framework.security.service.SecurityPermissionFrameworkService;

/**
 * @author Charlotte
 */
public interface ISysPermissionService extends SecurityPermissionFrameworkService, ICacheService {
    
    void processRoleDeleted(Long roleId);

}
