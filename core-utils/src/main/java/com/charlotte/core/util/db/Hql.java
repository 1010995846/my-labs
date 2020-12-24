package com.charlotte.core.util.db;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * hql语句，同时可具有 selection 和 table 的行为
 * Created by Charlotte on2020/5/21
 */
@Getter
public class Hql implements ISql {

    private List<ISelection> selections = new ArrayList<>();
    private ITable table;
    private IExpress express;
    private String groupBy;
    private String orderBy;

    public StringBuilder generateSql(){
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT ");
        if(selections == null || selections.size() == 0){
            builder.append("* ");
        } else {
            builder.append(StringUtils.join(selections.stream().map(ISelection::generate).collect(Collectors.toList()), ", "));
        }
        builder.append(" ");

        builder.append("FROM ");
        if(table == null){
            throw new RuntimeException();
        }
        builder.append(table.generate());

        if(express != null){
            builder.append("WHERE ");
            builder.append(express.generate());
        }

        if(groupBy != null){
            builder.append(groupBy);
        }

        if(orderBy != null){
            builder.append(orderBy);
        }

        return builder;
    }

    @Override
    public StringBuilder generate(){
        StringBuilder builder = new StringBuilder(generateSql());
        return builder;
    }

    public Hql addSelection(ISelection selection) {
        this.selections.add(selection);
        return this;
    }

    public Hql table(ITable table) {
        this.table = table;
        return this;
    }

    public Hql express(IExpress express) {
        this.express = express;
        return this;
    }

    public Hql groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public Hql orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

}



