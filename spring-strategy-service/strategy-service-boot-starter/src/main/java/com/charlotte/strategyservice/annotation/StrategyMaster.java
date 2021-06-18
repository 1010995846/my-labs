package com.charlotte.strategyservice.annotation;

import com.charlotte.strategyservice.config.StrategyBeanPostProcessor;
import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author Charlotte
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Primary
@Service
@Import({StrategyBeanPostProcessor.class})
public @interface StrategyMaster {

    /**
     * 指定策略路由
     */
    Class<? extends AbstractStrategyProxy> proxy() default AbstractStrategyProxy.class;

}
