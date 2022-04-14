package cn.cidea.framework.chain.annotation;

import cn.cidea.framework.chain.chain.ChainBeanNameGenerator;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Charlotte
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ChainScannerRegistrar.class)
public @interface ChainScan {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<? extends Annotation> annotationClass() default Chain.class;

    Class<? extends BeanNameGenerator> nameGenerator() default ChainBeanNameGenerator.class;
}
