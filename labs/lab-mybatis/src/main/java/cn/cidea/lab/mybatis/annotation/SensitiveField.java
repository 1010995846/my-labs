package cn.cidea.lab.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 标记敏感字段
 * @author Charlotte
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {
}
