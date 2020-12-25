package com.charlotte.lab.thread;

import java.util.concurrent.CountDownLatch;

public class Test2 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(4);
        Resource resource=Resource.getInstance();
        new Thread(()->{

            try {
                System.out.println(Thread.currentThread().getId()+"线程：请求减1");
                resource.minus(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }

        }).start();
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getId()+"线程：请求减1");
                resource.minus(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }

        }).start();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getId()+"线程：请求加1");
                resource.plus();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        }).start();
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getId()+"线程：请求长期持有锁");
                resource.minus("wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }

        }).start();
        countDownLatch.await();
        int result=resource.getI();
        System.out.println(result);
    }
}
