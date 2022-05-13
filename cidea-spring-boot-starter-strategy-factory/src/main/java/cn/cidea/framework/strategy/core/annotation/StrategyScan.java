package cn.cidea.framework.strategy.core.annotation;

import cn.cidea.framework.strategy.core.StrategyScannerRegistrar;
import cn.cidea.framework.strategy.core.factory.support.StrategyBeanNameGenerator;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 手动扫描
 * @author Charlotte
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(StrategyScannerRegistrar.class)
public @interface StrategyScan {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<? extends Annotation> annotationClass() default Strategy.class;

    Class<? extends BeanNameGenerator> nameGenerator() default StrategyBeanNameGenerator.class;
}
