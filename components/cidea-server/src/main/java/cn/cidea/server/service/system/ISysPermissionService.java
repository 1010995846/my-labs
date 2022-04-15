package cn.cidea.server.service.system;

import cn.cidea.server.dataobject.entity.SysRoleResource;
import cn.cidea.server.mybatis.ICacheService;
import cn.cidea.server.security.service.SecurityPermissionFrameworkService;

/**
 * @author Charlotte
 */
public interface ISysPermissionService extends SecurityPermissionFrameworkService, ICacheService {
    
    void processRoleDeleted(Long roleId);

}
