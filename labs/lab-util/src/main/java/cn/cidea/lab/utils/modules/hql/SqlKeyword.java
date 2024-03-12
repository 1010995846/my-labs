package cn.cidea.lab.utils.modules.hql;

import java.util.function.Supplier;

public enum SqlKeyword implements Supplier {
    AND("AND"),
    OR("OR"),
    IN("IN"),
    NOT("NOT"),
    LIKE("LIKE"),
    EQ("="),
    NE("<>"),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    GROUP_BY("GROUP BY"),
    HAVING("HAVING"),
    ORDER_BY("ORDER BY"),
    EXISTS("EXISTS"),
    BETWEEN("BETWEEN"),
    ASC("ASC"),
    DESC("DESC");

    private final String keyword;

    SqlKeyword(final String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Object get() {
        return this.keyword;
    }
}
