package cn.cidea.framework.mybatisplus.plugin.handlers;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Charlotte
 */
public abstract class AbstractJsonSetTypeHandler<E> extends AbstractJsonArrayTypeHandler<E> {

    @Override
    protected Collection<E> createCollection(Collection<E> collection) {
        return new HashSet<>(collection);
    }
}
