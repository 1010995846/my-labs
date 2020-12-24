package com.charlotte.strategyservice.autoconfig;

import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 默认自动配置
 * @author Charlotte
 */
@Slf4j
@Order
@ConditionalOnMissingBean(AbstractStrategyProxy.class)
public class DefaultStrategyProxyAutoConfig {

    @Bean
    @Scope("prototype")
    public AbstractStrategyProxy getStrategyProxy() {
        log.debug("use default proxy.");
        return new AbstractStrategyProxy() {
            @Override
            protected String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
                return null;
            }
        };
    }

}
