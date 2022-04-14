package cn.cidea.lab.se.annotation.inherited;

@InheritedAnnotation
public interface IInherited {

    @InheritedAnnotation
    public void invoke();

}
