package cn.cidea.framework.strategy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Charlotte
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@StrategyBranch("p2")
public @interface P2Branch {
}
