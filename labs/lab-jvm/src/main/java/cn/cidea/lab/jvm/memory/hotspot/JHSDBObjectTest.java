package cn.cidea.lab.jvm.memory.hotspot;

/**
 * staticObj、 instanceObj、 localObj的引用存放在哪里？
 * jps -l查询jvm进程
 * jhsdb hsdb --pid [jvmid]可视化工具
 * 查询地址，打开heap信息，查询堆内存地址区间
 * 检索对象，打开Console，scanoops [start] [end] [Class]查询堆中对象的内存地址
 * 查看对象信息，打开Inspector输入上面对象内存的地址
 * 查看地址在堆中的引用，revptrs [adress]
 */
public class JHSDBObjectTest {

    private static final int _1MB = 1024 * 1024;

    /**
     * -Xmx10m -XX:+UseSerialGC -XX:-UseCompressedOops
     */
    public static void main(String[] args) {
        Test test = new Test();
        test.foo();
        return;
    }

    static class Test {

        static ObjectHolder staticObj = new ObjectHolder();// 静态引用，堆
        ObjectHolder instanceObj = new ObjectHolder();// Test实例引用，堆

        void foo() {
            ObjectHolder localObj = new ObjectHolder();// 方法栈引用，栈
            System.out.println("done"); // 这里设一个断点
        }
    }

    private static class ObjectHolder {
        private int holder = _1MB * 3;
    }
}
