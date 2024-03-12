package cn.cidea.module.admin.service;

import cn.cidea.framework.mybatisplus.plugin.cache.ICacheService;
import cn.cidea.framework.security.core.service.ISecurityPermissionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Charlotte
 */
@Transactional(readOnly = true)
public interface IPermissionService extends ISecurityPermissionService, ICacheService {

    void processRoleDeleted(Long roleId);

    void exception();
}
