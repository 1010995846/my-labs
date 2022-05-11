package cn.cidea.framework.strategy.annotation;

import cn.cidea.framework.strategy.proxy.AbstractStrategyProxy;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * @author Charlotte
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StrategyBranches.class)
public @interface StrategyBranch {

    /**
     * 路由映射值
     * 对应{@link AbstractStrategyProxy#getRouteKeys(Object, Method, Object[], MethodProxy)}的返回值
     */
    String[] value();

}
