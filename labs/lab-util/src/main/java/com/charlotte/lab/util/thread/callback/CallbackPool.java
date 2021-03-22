package com.charlotte.lab.util.thread.callback;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Charlotte
 */
@Slf4j
public class CallbackPool {

    /**
     * 缓存队列
     */
    private static final ConcurrentHashMap<String, CallbackFutureTask> cache = new ConcurrentHashMap<>();

    /**
     * 过期队列
     */
    public static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    public static <T> T put(String key, long expire) throws Exception {
        CallbackFutureTask<T> futureTask = new CallbackFutureTask<T>(() -> {
            return 0;
        });
        cache.put(key, futureTask);
        if (expire > 0 && futureTask == null) {
            executorService.schedule(() -> {
                expire(key);
            }, expire, TimeUnit.MICROSECONDS);
        }
        return null;
    }

    private static void expire(String key) {
        Future future = cache.get(key);
        if (future != null) {
            if (!future.isDone()) {
                log.debug("Future任务被取消");
                future.cancel(true);
            }
            log.debug("缓存过期，缓存被清除");
            cache.remove(key);
        }
    }

    public static void callback(String key, Object obj) {
        CallbackFutureTask future = cache.get(key);
        future.setData(obj);

    }

    public static Object compute(String key) throws Exception {
        while (true) {
            Future future = cache.get(key);
            //A 1
            if (future == null) {
//                Callable callable = () -> cacheComputable.compute(key);
                Callable callable = () -> "0";
                FutureTask futureTask = new FutureTask(callable);
                //putIfAbsent 解决两个线程都进A 1 的情况
//                future = cache.putIfAbsent(key, futureTask);
                future = null;
                //B 2 第一次一定进B 2F
                if (future == null) {
                    future = futureTask;
                    //没必要多线程计算了。
                    futureTask.run();
                }
            }
            try {
                //已经有了，阻塞等待，如果计算异常，处理响应异常
                return future.get();
            } catch (CancellationException | InterruptedException e) {
                //取消任务异常 //中断异常
                //移出，防止future的污染
                cache.remove(key);
                e.printStackTrace();
                //再往外面抛
                throw e;
            } catch (ExecutionException e) {
                //执行异常，计算错误
                cache.remove(key);
                e.printStackTrace();
                //不抛了，while循环再次计算
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                System.out.println("收到通知");
                return "over";
            }
        };
        Runnable invoke = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    callable.call();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("任务开始");
        Thread thread = new Thread(invoke);
        thread.start();
        System.out.println("任务结束");
    }

}

@Setter
@Getter
class CallbackFutureTask<T> extends FutureTask {

    private Object data;

    public CallbackFutureTask(Callable callable) {
        super(callable);
    }

    public CallbackFutureTask(Runnable runnable, Object result) {
        super(runnable, result);
    }
}