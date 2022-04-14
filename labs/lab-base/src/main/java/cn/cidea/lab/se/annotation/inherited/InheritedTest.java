package cn.cidea.lab.se.annotation.inherited;

import java.lang.annotation.Annotation;

public class InheritedTest {

    public static void main(String[] args) {
        Annotation[] annotations = null;
        // 接口继承的注解
        annotations = IInherited.class.getAnnotations();
        // 不包含注解，接口之间不继承注解
        annotations = ISInherited.class.getAnnotations();
        annotations = InheritedServiceImpl.class.getAnnotations();

        // 类继承的注解
        annotations = InheritedService.class.getAnnotations();
        // 可继承@Inherited的注解
        annotations = InheritedSService.class.getAnnotations();

        annotations = SInheritedService.class.getAnnotations();
        /**
         * 总结，@Inherited注解的继承关系只对实现类、实现方法生效，对接口、接口方法、注解均不生效
         */

        annotations = SInheritedAnnotationService.class.getAnnotations();
        return;
    }

}
