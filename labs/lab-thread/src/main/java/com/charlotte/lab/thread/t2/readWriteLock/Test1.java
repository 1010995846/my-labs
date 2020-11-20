package com.charlotte.lab.thread.t2.readWriteLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试同一线程读写锁是否互斥
 */
public class Test1 {

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock rl = readWriteLock.readLock();
    private Lock wl = readWriteLock.writeLock();

    private void test1() {
        wl.lock();
        rl.lock();
        rl.unlock();
        wl.unlock();
        System.out.println("执行完毕。");
    }

    public static void main(String[] args) {
        new Test1().test1();
    }
}
