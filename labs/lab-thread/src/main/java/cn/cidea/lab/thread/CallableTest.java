package cn.cidea.lab.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<String> futureTask1 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return "完成Future1";
            }
        }) {
            @Override
            public void run() {
                System.out.println("执行Future1");
                super.run();
            }
        };


        FutureTask<String> futureTask2 = new FutureTask<String>(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行Futrue2");
            }
        }, "完成Future2");

        Thread thread1 = new Thread(futureTask1);
        thread1.start();
        new Thread(futureTask2).start();

        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());

    }


}
