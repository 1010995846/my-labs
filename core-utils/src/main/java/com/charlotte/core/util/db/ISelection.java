package com.charlotte.core.util.db;

public interface ISelection extends ISql {
    @Override
    public StringBuilder generate();

    public ISelection alias(String alias);
    public String getAlias();
}
