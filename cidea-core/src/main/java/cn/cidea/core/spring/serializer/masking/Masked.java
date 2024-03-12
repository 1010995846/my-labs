package cn.cidea.core.spring.serializer.masking;

import java.lang.annotation.*;

/**
 * 脱敏标注
 * 仅限{@link CharSequence}类型及其子类
 * @author: CIdea
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Masked {

    /**
     * 脱敏方法，默认全脱
     * @return
     */
    Class<? extends MaskAll> maskFunc() default MaskAll.class;

}