package cn.cidea.framework.strategy.config;

import cn.cidea.framework.strategy.annotation.StrategyRoute;
import cn.cidea.framework.strategy.proxy.AbstractStrategyProxy;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Charlotte
 */
@StrategyRoute
public class GunStrategyProxy extends AbstractStrategyProxy {

    public AtomicInteger i = new AtomicInteger(0);

    @Override
    protected String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
        // 轮询调用
        switch (i.getAndIncrement() % 3) {
            case 0:
                return new String[]{"AR15"};
            case 1:
                return new String[]{"HK416"};
            default:
                return null;
        }
    }
}
