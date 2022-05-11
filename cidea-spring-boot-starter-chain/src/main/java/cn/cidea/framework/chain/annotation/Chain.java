package cn.cidea.framework.chain.annotation;

import java.lang.annotation.*;

/**
 * 标识需要链路调用的接口
 * @author Charlotte
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Chain {
}
