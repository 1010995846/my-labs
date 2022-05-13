package cn.cidea.framework.strategy.config;

import cn.cidea.framework.strategy.core.IStrategyRoute;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 内部属性控制路由
 * @author Charlotte
 */
@Primary
@Component
public class DefaultDynamicStrategyRoute implements IStrategyRoute {

    public static ThreadLocal<String> routeKey = new ThreadLocal<>();

    @Override
    public String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
        return new String[]{routeKey.get()};
    }
}
