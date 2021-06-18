package com.charlotte.lab.jvm.memory.hotspot;

import java.util.ArrayList;
import java.util.List;

/**
 * jconsole打开视图工具，选择jvm进程
 */
public class JConsoleMemoryTest {
    /**
     * -Xms100m -Xmx100m -XX:+UseSerialGC
     */
    public static void main(String[] args) throws Exception {
        fillHeap(1000);
        System.gc();// ???回收失败
    }
    /**
     * 内存占位符对象， 一个OOMObject大约占64KB
     */
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            // 以64KB/50ms的速度向Java堆中填充数据， 一共填充1000次
            // 稍作延时， 令监视曲线的变化更加明显
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        list = null;// ???回收失败
        System.gc();
    }

}