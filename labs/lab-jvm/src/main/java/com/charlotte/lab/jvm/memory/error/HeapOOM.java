package com.charlotte.lab.jvm.memory.error;

import java.util.ArrayList;
import java.util.List;

/**
 * 2-3
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author Charlotte
 */
public class HeapOOM {

    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
        //java.lang.OutOfMemoryError: Java heap space
        //Dumping heap to java_pid3404.hprof ...
        //Heap dump file created [22045981 bytes in 0.663 secs]
    }
}
