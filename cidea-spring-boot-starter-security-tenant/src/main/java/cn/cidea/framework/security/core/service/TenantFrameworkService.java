package cn.cidea.framework.security.core.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Tenant 框架 Service 接口，定义获取租户信息
 */
@Validated
public interface TenantFrameworkService {

    /**
     * 获得所有租户
     *
     * @return 租户编号数组
     */
    List<Long> getTenantIds();

    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validTenant(@NotNull Long id);

}
