package com.charlotte.core.util.db;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
abstract class AbstractTable implements ITable {
    private String alias;
    private String join;
    private ITable joinTable;
    private IExpress express;

    @Override
    public StringBuilder generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(generateTable());
        if(StringUtils.isNotBlank(getAlias())){
            builder.append("AS ").append(getAlias()).append(" ");
        }
        if(StringUtils.isNotBlank(join)){
            builder.append(join).append(" ").append(joinTable.generate()).append("on ").append(express.generate());
        }
        return builder;
    }

    public abstract StringBuilder generateTable();

    @Override
    public ITable alias(String alias){
        this.alias = alias;
        return this;
    }

    @Override
    public ITable leftJoin(ITable joinTable, IExpress express){
        this.join = "LEFT JOIN ";
        this.joinTable = joinTable;
        this.express = express;
        return joinTable;
    }

    @Override
    public ITable rightJoin(ITable joinTable, IExpress express){
        this.join = "RIGHT JOIN ";
        this.joinTable = joinTable;
        this.express = express;
        return joinTable;
    }

    @Override
    public ITable innerJoin(ITable joinTable, IExpress express){
        this.join = "INNER JOIN ";
        this.joinTable = joinTable;
        this.express = express;
        return joinTable;
    }
}
