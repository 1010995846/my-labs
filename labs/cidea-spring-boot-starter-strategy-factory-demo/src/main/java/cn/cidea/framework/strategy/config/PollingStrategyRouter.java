package cn.cidea.framework.strategy.config;

import cn.cidea.framework.strategy.core.IStrategyRouter;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询路由
 * @author Charlotte
 */
@Component
public class PollingStrategyRouter implements IStrategyRouter {

    public AtomicInteger i = new AtomicInteger(0);

    @Override
    public String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
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
