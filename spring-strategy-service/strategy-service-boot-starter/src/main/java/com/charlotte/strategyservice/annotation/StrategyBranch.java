package com.charlotte.strategyservice.annotation;

import com.charlotte.strategyservice.autoconfig.DefaultStrategyProxyAutoConfig;
import com.charlotte.strategyservice.handler.StrategyBeanPostProcessor;
import com.charlotte.strategyservice.handler.StrategyScanBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Charlotte
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({StrategyScanBeanDefinitionRegistrar.class, StrategyBeanPostProcessor.class, DefaultStrategyProxyAutoConfig.class})
public @interface StrategyBranch {

    String[] value() default {};
}
