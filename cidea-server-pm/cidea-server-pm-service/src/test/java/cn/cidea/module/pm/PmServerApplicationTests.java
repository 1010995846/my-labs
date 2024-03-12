package cn.cidea.module.pm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PmServerApplication.class)
class PmServerApplicationTests {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @SneakyThrows
    @Test
    void contextLoads() {
        ListenableFuture<?> submit = taskExecutor.submitListenable(() -> {
            log.info("submit Callable");
            return "submit";
        });
        addCallback(submit);
        submit.get();

        submit = taskExecutor.submitListenable(() -> {
            log.info("submit Runnable");
        });
        addCallback(submit);
        submit.get();

        submit = taskExecutor.submitListenable(() -> {
            if(1== 1){
                throw new RuntimeException("EX");
            }
            return "";
        });
        addCallback(submit);
        try {
            submit.cancel(true);
            submit.get();
        } catch (ExecutionException e){
            // 线程执行的异常
            log.error("ExecutionException", e.getCause());
        } catch (Exception e){
            // 线程内部的异常
            log.error("Exception", e);
        }
    }

    private static void addCallback(ListenableFuture<?> submit) {
        submit.addCallback(result -> {
            log.info("addCallback succ");
        }, ex -> {
            // 依旧会抛出异常
            log.info("addCallback error");
        });
    }

}
