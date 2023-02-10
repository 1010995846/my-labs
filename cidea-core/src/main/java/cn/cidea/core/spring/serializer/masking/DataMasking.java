package cn.cidea.core.spring.serializer.masking;

import java.lang.annotation.*;

/**
 * @author: CIdea
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataMasking {

    DataMaskingFunc maskFunc() default DataMaskingFunc.NO_MASK;

}