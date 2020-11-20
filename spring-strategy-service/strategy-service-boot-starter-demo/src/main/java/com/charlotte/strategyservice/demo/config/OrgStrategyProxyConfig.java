package com.charlotte.strategyservice.demo.config;

import com.charlotte.strategyservice.autoconfig.DefaultStrategyProxyAutoConfig;
import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 *
 * @author Charlotte
 */
@Configuration
public class OrgStrategyProxyConfig extends DefaultStrategyProxyAutoConfig {

    @Override
    public OrgStrategyProxy getStrategyProxy() {
        return new OrgStrategyProxy();
    }

    public static class OrgStrategyProxy extends AbstractStrategyProxy {

        private int i = 0;

        @Override
        protected String getRouteKey(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
            // 轮询调用
            switch (i++%3){
                case 1:
                    return "school";
                case 2:
                    return "hosp";
                default:
                    return null;
            }
        }
    }
}
