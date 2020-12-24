package com.charlotte.lab.util.hql;

public interface ITable extends ISql {

    public String getAlias();

    public ITable alias(String alias);

    public ITable leftJoin(ITable table, IExpress express);

    public ITable rightJoin(ITable table, IExpress express);

    public ITable innerJoin(ITable table, IExpress express);
}
