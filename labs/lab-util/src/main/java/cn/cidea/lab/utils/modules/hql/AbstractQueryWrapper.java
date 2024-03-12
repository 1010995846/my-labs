package cn.cidea.lab.utils.modules.hql;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AbstractQueryWrapper<T, R, Children extends AbstractQueryWrapper<T, R, Children>> implements Compare<Children, R> {

    /**
     * 占位符
     */
    protected final Children typedThis = (Children) this;

    protected List<Supplier[]> expressList = new ArrayList<>();

    @Override
    public Children eq(boolean condition, R column, Object val) {
        if (condition) {
//            expressList.add(Express.equals(column, val));
            return doIt(condition, () -> columnToString(column), SqlKeyword.EQ, () -> formatSql("{0}", val));
        }
        return typedThis;
    }

    protected Children doIt(boolean condition, Supplier... sqlSegments) {
        if (condition) {
            expressList.add(sqlSegments);
        }
        return typedThis;
    }

    protected String columnToString(R column) {
        if (column instanceof String) {
            return (String) column;
        }
//        throw ExceptionUtils.mpe("not support this column !");
        return null;
    }

    protected final String formatSql(String sqlStr, Object... params) {
        return "sql";
    }

    @Override
    public Children ne(boolean condition, R column, Object val) {
        return typedThis;
    }
}
