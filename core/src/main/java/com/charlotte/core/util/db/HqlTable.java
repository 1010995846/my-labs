package com.charlotte.core.util.db;

import org.apache.commons.lang3.StringUtils;

public class HqlTable extends AbstractTable {

    private Hql table;

    public HqlTable(Hql table) {
        this.table = table;
    }

    @Override
    public StringBuilder generateTable() {
        StringBuilder builder = new StringBuilder("(").append(table.generate()).append(") ");
        return builder;
    }
}
