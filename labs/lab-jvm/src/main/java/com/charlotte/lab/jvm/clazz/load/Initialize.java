package com.charlotte.lab.jvm.clazz.load;

public class Initialize {

    public static void main(String[] args) {
        System.out.println(SuperClass.class);
//        init();
    }

    /**
     * 通过子类引用他父类的静态字段、静态方法：只初始化被引用的父类，会加载子类，但不会初始化
     */
    private static void init(){
        System.out.println(SubClass.value);
        SuperClass.print();
    }

    public static class SuperClass {
        static {
            System.out.println("SuperClass init!");
        }
        public static int value = 123;

        public static void print(){
            System.out.println("SuperClass print!");
        }
    }

    public static class SubClass extends SuperClass {
        static {
            System.out.println("SubClass init!");
        }
    }
}
