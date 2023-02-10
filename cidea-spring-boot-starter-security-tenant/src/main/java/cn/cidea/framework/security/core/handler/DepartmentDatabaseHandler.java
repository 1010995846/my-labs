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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 基于 MyBatis Plus 多租户的功能，实现 DB 层面的多租户的功能
 */
public class DepartmentDatabaseHandler implements TenantLineHandler {

    @Autowired
    private TenantProperties properties;
    private final String DEPARTMENT_ID = "department_id";

    @Override
    public String getTenantIdColumn() {
        return DEPARTMENT_ID;
    }

    @Override
    public Expression getTenantId() {
        LoginUserDTO loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return null;
        }
        Expression expression;
        if(CollectionUtils.isEmpty(loginUser.getDepartmentIds())){
            expression = new InExpression(new Column(getTenantIdColumn()), new ExpressionList(loginUser.getDepartmentIds().stream().map(LongValue::new).collect(Collectors.toList())));
        } else {
            expression = new IsNullExpression();
        }
        // 获取用户的各个角色及其权限范围，根据所属租户获取辖区租户列表
        return expression;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 情况一，全局未启用
        return !TenantContextHolder.enabledDepartment()
        // 情况二，忽略多租户的表
            || CollUtil.contains(properties.getIgnoreTables(), tableName);
    }

}
