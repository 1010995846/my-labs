// package cn.cidea.framework.strategy.core;
//
// import cn.cidea.framework.strategy.core.annotation.StrategyScan;
// import cn.cidea.framework.strategy.core.scanner.StrategyScannerConfigurer;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.support.BeanDefinitionBuilder;
// import org.springframework.beans.factory.support.BeanDefinitionRegistry;
// import org.springframework.beans.factory.support.BeanNameGenerator;
// import org.springframework.context.ResourceLoaderAware;
// import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
// import org.springframework.core.annotation.AnnotationAttributes;
// import org.springframework.core.io.ResourceLoader;
// import org.springframework.core.type.AnnotationMetadata;
// import org.springframework.core.type.StandardAnnotationMetadata;
// import org.springframework.util.ClassUtils;
// import org.springframework.util.StringUtils;
//
// import java.lang.annotation.Annotation;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * @author CIdea
//  */
// public class StrategyScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
//
//     @Override
//     public void setResourceLoader(ResourceLoader resourceLoader) {
//
//     }
//
//     @Override
//     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//         AnnotationAttributes annoAttrs = AnnotationAttributes
//                 .fromMap(importingClassMetadata.getAnnotationAttributes(StrategyScan.class.getName()));
//         if (annoAttrs == null) {
//             return;
//         }
//         registerBeanDefinitions(importingClassMetadata, annoAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
//     }
//
//     void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {
//
//         BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(StrategyScannerConfigurer.class);
//
//         // 以下都是获取注解信息，并进行相应设置
//         Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
//         if (!Annotation.class.equals(annotationClass)) {
//             builder.addPropertyValue("annotationClass", annotationClass);
//         }
//         // 生成nameGenerator实例
//         Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
//         if (!BeanNameGenerator.class.equals(generatorClass)) {
//             builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
//         }
//
//         // 待扫描包路径集合
//         List<String> basePackages = new ArrayList<>();
//         basePackages.addAll(
//                 Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
//         basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
//                 .collect(Collectors.toList()));
//         basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
//                 .collect(Collectors.toList()));
//         if (basePackages.size() == 0){
//             Class<?> introspectedClass = ((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass();
//             String defaultPackageName = introspectedClass.getPackage().getName();
//             basePackages.add(defaultPackageName);
//         }
//         builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
//
//         registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
//     }
//
//     private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
//         return importingClassMetadata.getClassName() + "#" + StrategyScannerRegistrar.class.getSimpleName() + "#" + index;
//     }
//
// }
