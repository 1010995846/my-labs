package cn.cidea.lab.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 标记敏感对象
 * @author Charlotte
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {
}
