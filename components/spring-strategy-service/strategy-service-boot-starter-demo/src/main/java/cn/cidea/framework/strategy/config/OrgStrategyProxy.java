package cn.cidea.framework.strategy.config;

import cn.cidea.framework.strategy.annotation.StrategyRoute;
import cn.cidea.framework.strategy.proxy.AbstractStrategyProxy;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Method;

/**
 * @author Charlotte
 */
@Primary
@StrategyRoute
public class OrgStrategyProxy extends AbstractStrategyProxy {

    public static ThreadLocal<String> routeKey = new ThreadLocal<>();

    @Override
    protected String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
        return new String[]{routeKey.get()};
    }
}
