package cn.cidea.server.mybatis.handlers;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Charlotte
 */
public abstract class AbstractSetTypeHandler<E> extends AbstractCollectionTypeHandler<E> {

    @Override
    protected Collection<E> createCollection(Collection<E> collection) {
        return new HashSet<>(collection);
    }
}
