// package cn.cidea.framework.strategy.core.annotation;
//
// import cn.cidea.framework.strategy.core.StrategyScannerRegistrar;
// import cn.cidea.framework.strategy.core.factory.StrategyBeanNameGenerator;
// import org.springframework.beans.factory.support.BeanNameGenerator;
// import org.springframework.context.annotation.Import;
//
// import java.lang.annotation.*;
//
// /**
//  * 手动扫描
//  * 扫描到的
//  * @author CIdea
//  */
// @Retention(RetentionPolicy.RUNTIME)
// @Target(ElementType.TYPE)
// @Documented
// @Import(StrategyScannerRegistrar.class)
// public @interface StrategyScan {
//
//     String[] value() default {};
//
//     String[] basePackages() default {};
//
//     Class<?>[] basePackageClasses() default {};
//
//     Class<? extends Annotation> annotationClass() default StrategyAPI.class;
//
//     Class<? extends BeanNameGenerator> nameGenerator() default StrategyBeanNameGenerator.class;
// }
