package com.charlotte.lab.jvm.memory.gc;

import org.junit.Test;

public class GCTests {

    private static final int _1MB = 1024 * 1024;
    /**
     * VM参数： -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:+PrintGCDetails：发生垃圾收集行为时打印内存回收日志， 并且在进程退出的时候输出当前的内存各区域分配情况
     * -XX:+PrintFlagsFinal：查看参数默认值
     */
    @Test
    public void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB]; // 出现一次Minor GC
    }

    /**
     * VM参数： -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
     */
    @Test
    public void testPretenureSizeThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB];
        //超过-XX:PretenureSizeThreshold大小的对象直接分配在老年代中
    }

    /**
     * VM参数： -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     * -XX:+PrintTenuringDistribution
     */
    @Test
    @SuppressWarnings("unused")
    public void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4]; // 什么时候进入老年代决定于XX:MaxTenuringThreshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }

    /**
     * VM参数： -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+PrintTenuringDistribution
     * 经试验，-XX:MaxTenuringThreshold，默认的值为7
     */
    @Test
    @SuppressWarnings("unused")
    public void testTenuringThresholdDefault() {
        byte[] allocation = new byte[_1MB];
        for (int i = 0; i < 800; i++) {
            // 不断触发Minor GC递增allocation的年龄，
            // 触发GC的新对象不能超过survior(1M)，不然会直接进入老年代
            byte[] allocation1 = new byte[_1MB / 2];
        }
    }
}
