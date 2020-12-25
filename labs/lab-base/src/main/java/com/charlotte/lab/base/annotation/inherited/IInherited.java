package com.charlotte.lab.base.annotation.inherited;

@InheritedAnnotation
public interface IInherited {

    @InheritedAnnotation
    public void invoke();

}
