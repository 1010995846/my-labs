package com.charlotte.strategyservice.demo.config;

import com.charlotte.strategyservice.annotation.StategyRoute;
import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Method;

/**
 * @author Charlotte
 */
@Primary
@StategyRoute
public class OrgStrategyProxy extends AbstractStrategyProxy {

    public static ThreadLocal<String> routeKey = new ThreadLocal<>();

    @Override
    protected String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
        return new String[]{routeKey.get()};
    }
}
