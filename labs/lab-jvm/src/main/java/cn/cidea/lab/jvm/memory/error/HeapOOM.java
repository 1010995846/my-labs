package cn.cidea.lab.jvm.memory.error;

import java.util.ArrayList;
import java.util.List;

/**
 * 2-3
 * 堆溢出，不断创建对象并保持引用不回收
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author Charlotte
 */
public class HeapOOM {

    static class OOMObject {
        byte[] bytes = new byte[1024*1024];
    }

    public static void main(String[] args) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
            Thread.sleep(100L);
        }
        //java.lang.OutOfMemoryError: Java heap space
        //Dumping heap to java_pid3404.hprof ...
        //Heap dump file created [22045981 bytes in 0.663 secs]
    }
}
