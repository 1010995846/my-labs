package cn.cidea.lab.jvm.clazz;

/**
 * Filed的解析
 * @author Charlotte
 */
public class FieldResolution {
    public static void main(String[] args) {
        // 若注释Sub#A会导致编译器的歧义，即使类的字段解析有先后顺序（先自己、后接口、后父类，自下而上），即使所有类/接口的A解析后确定的是同一个访问字段的直接引用
        // Reference to 'A' is ambiguous, both 'Parent.A' and 'Interface2.A' match
        System.out.println(Sub.A);
    }

    interface Interface0 {
        int A = 0;
    }

    interface Interface1 extends Interface0 {
        int A = 4;
    }

    interface Interface2 {
        final int A = 4;
    }

    static class Parent implements Interface1 {
        public static final int A = 4;
    }

    static class Sub extends Parent implements Interface2 {
        public static final int A = 4;
    }

}
