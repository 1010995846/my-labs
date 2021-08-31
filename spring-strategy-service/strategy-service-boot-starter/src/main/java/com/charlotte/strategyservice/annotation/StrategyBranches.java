package com.charlotte.strategyservice.annotation;


import java.lang.annotation.*;

/**
 * @author Charlotte
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyBranches {

    StrategyBranch[] value();

}
