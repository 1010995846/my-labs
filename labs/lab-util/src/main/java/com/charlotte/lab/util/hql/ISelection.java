package com.charlotte.lab.util.hql;

public interface ISelection extends ISql {
    @Override
    public StringBuilder generate();

    public ISelection alias(String alias);

    public String getAlias();
}
