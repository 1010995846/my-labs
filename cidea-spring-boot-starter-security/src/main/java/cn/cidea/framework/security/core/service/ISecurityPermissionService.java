package cn.cidea.framework.security.core.service;

/**
 * Security 框架 Permission Service 接口，定义 security 组件需要的功能
 * 权限认证相关
 * @author Charlotte
 */
public interface ISecurityPermissionService {

    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    boolean hasPermission(String permission);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(String... permissions);

    /**
     * 判断是否有角色
     *
     * 注意，角色使用的是 SysRoleDO 的 code 标识
     *
     * @param code 角色
     * @return 是否
     */
    boolean hasRole(String code);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param codes 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(String... codes);

    boolean superAdmin();
}
