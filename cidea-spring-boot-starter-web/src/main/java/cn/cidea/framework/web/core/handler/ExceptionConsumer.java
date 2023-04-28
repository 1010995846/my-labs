package cn.cidea.framework.web.core.handler;

import cn.cidea.framework.web.core.api.Response;
import cn.hutool.core.util.ClassUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: CIdea
 */
public abstract class ExceptionConsumer<E extends Throwable> {

    public boolean match(Throwable t){
        return ClassUtil.isAssignable(ec(), t.getClass());
    }

    protected abstract Class ec();

    abstract Response<?> handler(HttpServletRequest req, E ex);

}
