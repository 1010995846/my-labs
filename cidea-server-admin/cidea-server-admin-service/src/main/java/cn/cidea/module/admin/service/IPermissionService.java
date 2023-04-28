package cn.cidea.module.admin.service;

import cn.cidea.framework.mybatisplus.plugin.cache.ICacheService;
import cn.cidea.framework.security.core.service.ISecurityPermissionService;

/**
 * @author Charlotte
 */
public interface IPermissionService extends ISecurityPermissionService, ICacheService {

    void processRoleDeleted(Long roleId);

}
