package com.charlotte.core.util.db;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * 标准字段表达式
 */
public class Param implements ISelection {
    private ITable table;
    private String param;
    private String alias;

    public Param(String param) {
        this.table = table;
        this.param = param;
    }

    public Param(ITable table, String param) {
        this.table = table;
        this.param = param;
    }

    @Override
    public StringBuilder generate() {
        StringBuilder builder = new StringBuilder();
        if(table != null){
            // 字段归属
            if(StringUtils.isNotBlank(table.getAlias())){
                builder.append(table.getAlias()).append(".");
            }
        }
        builder.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, param));
        return builder;
    }

    @Override
    public Param alias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public Param table(ITable table){
        this.table = table;
        return this;
    }
}
