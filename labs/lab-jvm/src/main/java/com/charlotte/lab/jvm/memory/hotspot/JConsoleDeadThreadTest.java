package com.charlotte.lab.jvm.memory.hotspot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JConsoleDeadThreadTest {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new SynAddRunalbe(1, 2)).start();
            new Thread(new SynAddRunalbe(2, 1)).start();
        }
    }
    /**
     * 线程死锁等待演示
     * BLOCKED
     */
    static class SynAddRunalbe implements Runnable {
        int a, b;

        public SynAddRunalbe(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            // Integer.valueOf()方法出于减少对象创建次数和节省内存的考虑， 会对数值为-128～127之间的Integer对象进行缓存
            // 此例中造成了竞争同一个Integer类型1、2的死锁
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }
        }
    }
}
