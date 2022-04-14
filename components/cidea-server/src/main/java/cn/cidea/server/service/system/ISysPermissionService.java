package cn.cidea.server.service.system;

import cn.cidea.server.security.service.SecurityPermissionFrameworkService;

/**
 * @author Charlotte
 */
public interface ISysPermissionService extends SecurityPermissionFrameworkService {
    
    void processRoleDeleted(Long roleId);
}
