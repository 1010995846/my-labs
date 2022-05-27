package cn.cidea.framework.security.config;

import cn.cidea.framework.security.core.aspect.TenantAspect;
import cn.cidea.framework.security.core.filter.TenantContextWebFilter;
import cn.cidea.framework.security.core.filter.TenantSecurityWebFilter;
import cn.cidea.framework.security.core.handler.DepartmentDatabaseHandler;
import cn.cidea.framework.security.core.handler.TenantDatabaseHandler;
import cn.cidea.framework.security.core.properties.TenantProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlotte
 */
@Configuration
public class TenantAutoConfiguration {

    @Bean
    public TenantProperties tenantProperties() {
        return new TenantProperties();
    }

    @Bean
    public TenantContextWebFilter tenantContextWebFilter() {
        return new TenantContextWebFilter();
    }

    @Bean
    public TenantSecurityWebFilter tenantSecurityWebFilter() {
        return new TenantSecurityWebFilter();
    }


    @Bean
    public TenantDatabaseHandler tenantDatabaseInterceptor() {
        return new TenantDatabaseHandler();
    }

    @Bean
    public TenantAspect tenantAspect() {
        return new TenantAspect();
    }

    @Bean
    public DepartmentDatabaseHandler departmentDatabaseHandler() {
        return new DepartmentDatabaseHandler();
    }

    @Bean
    public TenantLineInnerInterceptor TenantLineInnerInterceptor(MybatisPlusInterceptor interceptor) {
        TenantLineInnerInterceptor tenantInner = new TenantLineInnerInterceptor(tenantDatabaseInterceptor());
        TenantLineInnerInterceptor departmentInner = new TenantLineInnerInterceptor(departmentDatabaseHandler());
        // 防止获取到NULL导致NPE
        List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
        inners.add(0, departmentInner);
        inners.add(0, tenantInner);
        interceptor.setInterceptors(inners);
        return tenantInner;
    }

}
