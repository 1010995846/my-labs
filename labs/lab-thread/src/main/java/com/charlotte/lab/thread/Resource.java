package com.charlotte.lab.thread;

import java.util.concurrent.TimeUnit;

public class Resource {

    private Resource() {


    }

    private static final Resource resource = new Resource();

    private int i=0;

    public int getI() {
        return i;
    }

    public static Resource getInstance() {
        return resource;
    }

    public synchronized void plus() throws InterruptedException {
        if (i==1){
            System.out.println(Thread.currentThread().getId()+"线程：加等待");
            wait();
        }
        System.out.println("睡眠1秒，标识+1");
        TimeUnit.SECONDS.sleep(1);
        i++;
        System.out.println(Thread.currentThread().getId()+"线程：加成功，通知其他线程");
        notifyAll();
    }

    public synchronized void minus(String wait) throws InterruptedException {
        if ("wait".equals(wait)){
            System.out.println(Thread.currentThread().getId()+"线程：持有锁10秒");
            TimeUnit.SECONDS.sleep(10);
        }
        if (i==0){
            System.out.println(Thread.currentThread().getId()+"线程：减等待");
            wait();
        }
        i--;
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread().getId()+"线程：减成功，不通知其他线程");
//        notify();

    }

}
