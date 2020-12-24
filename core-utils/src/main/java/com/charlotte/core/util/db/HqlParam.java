package com.charlotte.core.util.db;

import org.apache.commons.lang3.StringUtils;

public class HqlParam implements ISelection {

    private Hql table;
    private String alias;

    public HqlParam(Hql table) {
        this.table = table;
    }

    @Override
    public StringBuilder generate() {
        StringBuilder builder = new StringBuilder("(").append(table.generate()).append(") ");
        if(StringUtils.isNotBlank(getAlias())){
            builder.append("AS ").append(getAlias()).append(" ");
        }
        return builder;
    }

    @Override
    public HqlParam alias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getAlias() {
        return alias;
    }
}
