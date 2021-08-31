package com.charlotte.strategyservice.demo.annotation;

import com.charlotte.strategyservice.annotation.StrategyBranch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Charlotte
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@StrategyBranch("p1")
public @interface P1Branch {
}
