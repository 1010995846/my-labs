package cn.cidea.core.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 同步工具
 * 锁、事务
 * 引了redisson、spring-tx、spring-context
 * @author: CIdea
 * @version 2022-12-08
 */
@Slf4j
@Component
@ConditionalOnClass({RedissonClient.class, ThreadPoolTaskExecutor.class, PlatformTransactionManager.class})
public class SynchronizedUtils implements ApplicationContextAware {

    @Autowired(required = false)
    private ThreadPoolTaskExecutor threadPoolExecutor;
    @Autowired(required = false)
    private PlatformTransactionManager transactionManager;
    @Autowired(required = false)
    private RedissonClient redissonClient;

    private static SynchronizedUtils self;

    /**
     * 事务提交后执行
     */
    public static void afterTrxCommit(Runnable runnable){
        if(!TransactionSynchronizationManager.isActualTransactionActive()){
            runnable.run();
        } else {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("run after transaction commit");
                    runnable.run();
                }
            });
        }
    }
    public static void beforeTrxCommit(Runnable runnable){
        if(!TransactionSynchronizationManager.isActualTransactionActive()){
            runnable.run();
        } else {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    log.info("run before transaction commit");
                    runnable.run();
                }
            });
        }
    }

    /**
     * 新线程执行
     */
    public static void newThr(Runnable runnable){
        self.threadPoolExecutor.execute(() -> {
            log.info("new thread start");
            runnable.run();
            log.info("new thread end");
        });
    }

    /**
     * 事务提交后新线程执行
     */
    public static void afterTrxCommitAndNewThr(Runnable runnable){
        afterTrxCommit(() -> newThr(runnable));
    }

    /**
     * 新事务，嵌套时注意避免行锁
     */
    public static void newTrx(Runnable runnable){
        newTrx(() -> {
            runnable.run();
            return null;
        });
    }
    public static <T> T newTrx(Supplier<T> supplier){
        log.info("new transaction start");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus ts = self.transactionManager.getTransaction(def);
        try {
            return supplier.get();
        } catch (RuntimeException e) {
            ts.setRollbackOnly();
            log.warn("new transaction rollback");
            throw e;
        } finally {
            self.transactionManager.commit(ts);
            log.info("new transaction commit");
        }
    }

    /**
     * 锁
     */
    public static void lock(String name, Runnable runnable){
        lock(Collections.singleton(name), () -> {
            runnable.run();
            return null;
        });
    }
    public static <T> T lock(String name, Supplier<T> supplier){
        return lock(Collections.singleton(name), supplier);
    }
    public static void lock(Collection<String> names, Runnable runnable){
        lock(names, () -> {
            runnable.run();
            return null;
        });
    }
    public static void lock(String preffix, Collection<?> names, Runnable supplier){
        Set<String> keys = names.parallelStream()
                .map(name -> preffix + name)
                .collect(Collectors.toSet());
        lock(keys, supplier);
    }

    public static <T> T lock(String preffix, Collection<?> names, Supplier<T> supplier){
        Set<String> keys = names.stream()
                .map(name -> preffix + name)
                .collect(Collectors.toSet());
        return lock(keys, supplier);
    }
    public static <T> T lock(Collection<String> names, Supplier<T> supplier){
        if(!(names instanceof Set)){
            names = names.stream().collect(Collectors.toSet());
        }
        Assert.notEmpty(names, "lock names not be empty!");
        RLock lock;
        Iterator<String> iterator = names.iterator();
        if(names.size() == 1) {
            lock = self.redissonClient.getLock(iterator.next());
        } else {
            RLock[] subLocks = new RLock[names.size()];
            for (int i = 0; i < names.size(); i++) {
                subLocks[i] = self.redissonClient.getLock(iterator.next());
            }
            lock = self.redissonClient.getMultiLock(subLocks);
        }
        boolean b;
        log.info("try lock for {}", JSONObject.toJSONString(names));
        try {
            b = lock.tryLock(6, 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("redisson tryLock error!", e);
            throw new RuntimeException("系统繁忙，请稍后重试");
        }
        if(!b){
            throw new RuntimeException("系统处理中，请勿重复点击");
        }
        log.info("locked for {}", JSONObject.toJSONString(names));
        try {
            return supplier.get();
        } finally {
            try {
                lock.unlock();
                log.info("unlock for {}", JSONObject.toJSONString(names));
            } catch (Exception e){
                log.error("unlock error! key = " + JSONObject.toJSONString(names), e);
            }
        }
    }

    /**
     * 全局锁并发起新事务（刷新状态，保证事务中状态是最新的）
     */
    public static <T> T lockNewTrx(Collection<String> names, Supplier<T> supplier){
        return lock(names, () -> newTrx(supplier));
    }
    public static void lockNewTrx(Collection<String> names, Runnable runnable){
        lock(names, () -> newTrx(runnable));
    }

    public static <T> T lockNewTrx(String name, Supplier<T> supplier){
        return lock(name, () -> newTrx(supplier));
    }
    public static void lockNewTrx(String name, Runnable runnable){
        lock(name,  () -> newTrx(runnable));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        self = applicationContext.getBean(SynchronizedUtils.class);
    }

}
