package cn.cidea.module.admin.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内置角色标识枚举
 */
@Getter
@AllArgsConstructor
public enum BuiltInRole {

    SYS_ADMIN("sys_admin", "系统管理员"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    ;

    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        return SYS_ADMIN.getCode().equals(code);
    }

}
