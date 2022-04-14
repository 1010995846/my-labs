package cn.cidea.lab.jvm.memory.error;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Args： -Xmx20M -XX:MaxDirectMemorySize=10M
 * 指定直接内存大小，否则默认与-Xmx一致
 *
 * @author zzm
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        // 直接内存溢出
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
//            Thread.sleep(100L);
        }
        // Exception in thread "main" java.lang.OutOfMemoryError
        // 排查点：Heap无明显异常，直接或间接使用了DirectMemory
    }
}
