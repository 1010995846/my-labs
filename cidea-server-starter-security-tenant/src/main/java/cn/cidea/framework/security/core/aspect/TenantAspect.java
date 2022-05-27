package cn.cidea.framework.security.core.aspect;

import cn.cidea.framework.security.core.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 忽略多租户的 Aspect，基于 {@link Tenant} 注解实现，用于一些全局的逻辑。
 * 例如说，一个定时任务，读取所有数据，进行处理。
 * 又例如说，读取所有数据，进行缓存。
 */
@Aspect
@Slf4j
public class TenantAspect {

    @Around("@annotation(tenant)")
    public Object around(ProceedingJoinPoint joinPoint, Tenant tenant) throws Throwable {
        Boolean oldTenantEnabled = TenantContextHolder.enabled();
        Boolean oldDepartmentEnabled = TenantContextHolder.enabledDepartment();
        try {
            TenantContextHolder.setEnabled(true);
            TenantContextHolder.setEnabled(tenant.department());
            // 执行逻辑
            return joinPoint.proceed();
        } finally {
            TenantContextHolder.setEnabled(oldTenantEnabled);
            TenantContextHolder.setEnabledDepartment(oldDepartmentEnabled);
        }
    }

}
