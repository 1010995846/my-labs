package cn.cidea.lab.jvm.memory.error;

import java.util.HashSet;
import java.util.Set;

/**
 * VM Args：-XX:PermSize=6M -XX:MaxPermSize=6M
 * 常量池OOM
 * @author zzm
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        // 使用Set保持着常量池引用，避免Full GC回收常量池行为
        Set<String> set = new HashSet<>();
        // 在short范围内足以让6MB的PermSize产生OOM了
        long i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}