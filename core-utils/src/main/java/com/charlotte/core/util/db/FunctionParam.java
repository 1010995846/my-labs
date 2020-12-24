package com.charlotte.core.util.db;

import org.apache.commons.lang3.StringUtils;

/**
 * 函数表达式
 */
public class FunctionParam implements ISelection {
    private String function;
    private ISelection[] params;
    private String alias;

    public FunctionParam(String function, ISelection... params) {
        this.function = function;
        this.params = params;
    }

    @Override
    public StringBuilder generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(function).append("(");
        for (ISelection param : params) {
            builder.append(param.generate()).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") ");
        if(StringUtils.isNotBlank(getAlias())){
            builder.append("AS ").append(getAlias());
        }
        return builder;
    }

    @Override
    public ISelection alias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getAlias() {
        return alias;
    }

}
