package cn.cidea.framework.security.core.aspect;

import java.lang.annotation.*;

/**
 * 开启租户标志，标记指定方法进行租户的自动过滤
 * <p>
 * 注意，只有 DB 的场景会过滤，其它场景暂时不过滤：
 * @author Charlotte
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Tenant {

    /**
     * 是否启用部门
     * @return
     */
    boolean department() default true;

}
