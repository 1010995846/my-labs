package cn.cidea.framework.strategy.core.annotation;

import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * 策略分支
 * @author Charlotte
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StrategyBranches.class)
@Service
public @interface StrategyBranch {

    /**
     * 路由映射值
     * 对应{@link cn.cidea.framework.strategy.core.IStrategyRoute#getRouteKeys(Object, Method, Object[], MethodProxy)}的返回值
     */
    String[] value();

}
