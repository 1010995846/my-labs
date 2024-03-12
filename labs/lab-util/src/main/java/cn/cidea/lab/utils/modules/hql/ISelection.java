package cn.cidea.lab.utils.modules.hql;

public interface ISelection extends ISql {
    @Override
    public StringBuilder generate();

    public ISelection alias(String alias);

    public String getAlias();
}
