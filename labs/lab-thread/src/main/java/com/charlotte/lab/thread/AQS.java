package com.charlotte.lab.thread;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class AQS extends AbstractQueuedSynchronizer {

    private static int num = 0;

    public AQS() {
        num++;
    }

    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }

    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }

    @Override
    protected int tryAcquireShared(int arg) {
        return super.tryAcquireShared(arg);
    }

    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }

    @Override
    protected boolean isHeldExclusively() {
        return super.isHeldExclusively();
    }


    private static final Unsafe unsafe;
    private Thread thread = new Thread("old");
    private Thread threadCopy = thread;
    //    thread在class中的偏移地址
    private static final long threadOffset;

    private ReentrantLock reentrantLock = new ReentrantLock(true);

    public AQS get() {
        reentrantLock.lock();
        System.out.println("Thread name: " + Thread.currentThread());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AQS aqs = new AQS();
        reentrantLock.unlock();
        return aqs;
    }

    static {
        try {
//            unsafe = Unsafe.getUnsafe();
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            threadOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("thread"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public static void main(String[] args) {
//        AQS aqs = new AQS();
////        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
//        Thread node = new Thread("new");
//        Thread pred = aqs.thread;
//        Thread d = pred;
//        System.out.println("operation: " + unsafe.compareAndSwapObject(aqs, AQS.threadOffset, pred, node));
////        aqs.thread被覆写，aqs.copy、t、d却不变
//        System.out.println("state: " + aqs.thread);//10
//        System.out.println("next: " + node);//10
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//               aqs.get();
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//               aqs.get();
//            }
//        }).start();

        ReentrantLock lock = new ReentrantLock();
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    lock.lock();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.unlock();
                }
            }, "线程-" + i).run();
        }


    }

}
