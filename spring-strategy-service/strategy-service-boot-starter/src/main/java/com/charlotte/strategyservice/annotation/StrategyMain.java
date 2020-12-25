package com.charlotte.strategyservice.annotation;

import com.charlotte.strategyservice.autoconfig.DefaultStrategyProxyAutoConfig;
import com.charlotte.strategyservice.handler.StrategyBeanPostProcessor;
import com.charlotte.strategyservice.handler.StrategyScanBeanDefinitionRegistrar;
import com.charlotte.strategyservice.proxy.AbstractStrategyProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author Charlotte
 */
@Primary
@Service
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({StrategyScanBeanDefinitionRegistrar.class, StrategyBeanPostProcessor.class, DefaultStrategyProxyAutoConfig.class})
public @interface StrategyMain {

//    Class<? extends AbstractStrategyProxy> proxy() default AbstractStrategyProxy.class;

}
