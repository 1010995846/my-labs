package com.charlotte.core.util.db;

import lombok.Getter;

@Getter
public class Table<T> extends AbstractTable {

    private String table;

    public Table(String table) {
        this.table = table;
    }

    @Override
    public StringBuilder generateTable(){
        return new StringBuilder(table).append(" ");
    }

}
