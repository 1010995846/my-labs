package cn.cidea.server.mybatis.handlers;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Charlotte
 */
public abstract class AbstractCollectionTypeHandler<E> extends AbstractJsonTypeHandler<Collection<E>> {

    abstract Class<E> getTargetClass();

    protected Collection<E> createCollection(Collection<E> collection){
        return collection;
    }

    @Override
    protected Collection<E> parse(String json) {
        Collection<E> list = JSONArray.parseArray(json, getTargetClass());
        if(list == null){
            list = new ArrayList<>(0);
        }
        return createCollection(list);
    }

    @Override
    protected String toJson(Collection<E> obj) {
        if(obj == null){
            obj = Collections.EMPTY_LIST;
        }
        return JSONArray.toJSONString(obj);
    }

    @Override
    public Collection<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        final String json = rs.getString(columnName);
        return parse(json);
    }

    @Override
    public Collection<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        final String json = rs.getString(columnIndex);
        return parse(json);
    }

    @Override
    public Collection<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        final String json = cs.getString(columnIndex);
        return parse(json);
    }
}
