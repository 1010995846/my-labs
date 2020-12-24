package com.charlotte.core.util.lab;

import com.charlotte.core.util.db.*;

/**
 * Created by Charlotte on2020/5/21
 */
public class HqlUtil {


    public static void main(String[] args) {
        Hql hql = new Hql();

        Hql innerHql = new Hql();
        innerHql.table(new Table("order_detail"));

        // todo 添加join、join子查询
        ITable table = new Table("hos_order").alias("order");
        table.leftJoin(new HqlTable(innerHql).alias("detail"), Express.equals("delFlag", "0"));
        hql.table(table);
//        table.

        // todo 添加函数式、子语句
        hql.addSelection(new Param("orderId").table(table).alias("orderId"));
        hql.addSelection(new FunctionParam("MAX", new Param("infoId"), new Param("id")).alias("max"));
        hql.addSelection(new HqlParam(innerHql).alias("datailItem"));

        // todo 添加where子查询
//        Express express = Express.equals("a", "1")
//                .orPrior(Express.equals("orderId", "2").and(Express.notEquals("g", "8")))
//                .and(Express.equals("orderNo", "3"))
//                .and(Express.equals("d", "4"))
//                .and(Express.in("orderState", values))
//                .and(Express.isNull("nullParameter"));
//        hql.express(express);
        System.out.println(hql.generateSql().toString());
        return;
    }
}
