package com.charlotte.lab.base.annotation.inherited;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InheritedAnnotation
public @interface SInheritedAnnotation {
}
