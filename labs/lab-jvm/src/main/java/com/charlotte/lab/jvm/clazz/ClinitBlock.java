package com.charlotte.lab.jvm.clazz;

/**
 * <clinit>初始化方法仅执行一次，由静态变量初始化和静态块组成
 * 本例中仅打印一次<clinit>，然后阻塞
 * 导致其余线程阻塞，等待要使用的类初始化完成
 * @author Charlotte
 */
public class ClinitBlock {

    public static void main(String[] args) {
        Runnable script = () -> {
            System.out.println(Thread.currentThread() + "start");
            // new类，触发初始化
            DeadLoopClass dlc = new DeadLoopClass();
            System.out.println(Thread.currentThread() + " run over");
        };
        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }

    static class DeadLoopClass {

        static {
            // 如果不加上这个if语句， 编译器将提示“Initializer does not complete normally”并拒绝编译
            if (true) {
                //
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while (true) {
                }
            }
        }
    }

}
