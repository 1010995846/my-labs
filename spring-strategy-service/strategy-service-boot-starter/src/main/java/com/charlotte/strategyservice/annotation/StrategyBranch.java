package com.charlotte.strategyservice.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Charlotte
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface StrategyBranch {

    String[] value() default {};
}
