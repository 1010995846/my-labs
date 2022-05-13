package cn.cidea.framework.strategy.core.annotation;

import cn.cidea.framework.strategy.core.IStrategyRoute;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * 标记策略端口，端口可以是类或接口
 * 被标记的类会生成一个新的代理类注入Spring中，并标记为{@link org.springframework.context.annotation.Primary}
 * 分支实现标记{@link StrategyBranch}
 * 主干实现标记{@link StrategyMaster}
 * 分支根据{@link IStrategyRoute#getRouteKeys(Object, Method, Object[], MethodProxy)}、{@link StrategyBranch#value()}匹配
 *
 * 注意端口子类或实现类不可标记{@link org.springframework.context.annotation.Primary}注解
 * @author Charlotte
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StrategyPort {

    /**
     * 指定策略路由
     */
    Class<? extends IStrategyRoute> route() default IStrategyRoute.class;
}
