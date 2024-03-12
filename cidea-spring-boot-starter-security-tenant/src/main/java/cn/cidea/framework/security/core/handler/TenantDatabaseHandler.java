package cn.cidea.framework.security.core.handler;

import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.security.core.context.TenantContextHolder;
import cn.cidea.framework.security.core.properties.TenantProperties;
import cn.cidea.framework.security.core.utils.SecurityFrameworkUtils;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * mybatis3.4.x才有实现
 * 基于 MyBatis Plus 多租户的功能，实现 DB 层面的多租户的功能
 * 结合拦截器{@link com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor#TenantLineInnerInterceptor(TenantLineHandler)}使用
 * 也可以用{@link com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler}和{@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
 */
public class TenantDatabaseHandler implements TenantLineHandler {

    @Autowired
    private TenantProperties properties;

    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return null;
        }
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 情况一，全局未启用
        return !TenantContextHolder.enabled()
                // 情况二，忽略多租户的表
                || CollUtil.contains(properties.getIgnoreTables(), tableName);
    }

}
