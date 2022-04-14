package cn.cidea.lab.thread;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AnonymousPoolTest {

//    ExecutorService executorService = new ThreadPoolExecutor(
//            20,
//            2147483647,
//            60L,
//            TimeUnit.SECONDS,
//            new SynchronousQueue<>(),
//            new ThreadFactory() {
//
//                private final AtomicInteger threadNumber = new AtomicInteger(1);
//
//                @Override
//                public Thread newThread(Runnable r) {
//                    return new Thread(r, "anonymous-" + threadNumber);
//                }
//            });
    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    private static final int _1MB = 1024 * 1024;

    @Test
    public void execute() throws InterruptedException {
        while (true){
            byte[] bytes = new byte[_1MB * 1000];
            Hold hold = new Hold(bytes);
            executorService.execute(() -> {
                hold.invoke();
            });
            Thread.sleep(1000L);
        }
    }

}


@AllArgsConstructor
class Hold{

    private static final AtomicInteger number = new AtomicInteger(1);
    byte[] bytes;

    public void invoke() {
        int i = number.getAndIncrement();
        System.out.println("invoke---------" + i);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("invoke=========" + i);
    }
}