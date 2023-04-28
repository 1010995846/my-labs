package cn.cidea.framework.strategy.core.annotation;

import cn.cidea.framework.strategy.core.IStrategyRouter;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * 策略分支
 * @author CIdea
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StrategyBranches.class)
@Service
public @interface StrategyBranch {

    /**
     * 路由映射值
     * 对应{@link IStrategyRouter#getRouteKeys(Object, Method, Object[], MethodProxy)}的返回值
     */
    String[] value();

}
