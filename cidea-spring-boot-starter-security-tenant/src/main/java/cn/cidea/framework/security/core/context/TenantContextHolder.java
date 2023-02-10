package cn.cidea.framework.security.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 多租户上下文 Holder
 */
public class TenantContextHolder {

    /**
     * 当前租户编号
     */
    private static final ThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 是否忽略租户
     */
    private static final ThreadLocal<Boolean> ENABLED = new TransmittableThreadLocal<>();

    private static final ThreadLocal<Boolean> ENABLED_DEPARTMENT = new TransmittableThreadLocal<>();

    /**
     * 获得租户编号。
     *
     * @return 租户编号
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * 获得租户编号。如果不存在，则抛出 NullPointerException 异常
     *
     * @return 租户编号
     */
    public static Long getRequiredTenantId() {
        Long tenantId = getTenantId();
        if (tenantId == null) {
            throw new NullPointerException("TenantContextHolder 不存在租户编号");
        }
        return tenantId;
    }

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static void setEnabled(Boolean enabled) {
        ENABLED.set(enabled);
    }

    public static void setEnabledDepartment(Boolean enabled) {
        ENABLED_DEPARTMENT.set(enabled);
    }

    /**
     * 当前是否启用租户
     *
     * @return 是否启用
     */
    public static boolean enabled() {
        return Boolean.TRUE.equals(ENABLED.get());
    }

    public static boolean enabledDepartment() {
        return Boolean.TRUE.equals(ENABLED_DEPARTMENT.get());
    }

    public static void clear() {
        TENANT_ID.remove();
        ENABLED.remove();
    }

}
