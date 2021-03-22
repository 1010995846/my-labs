package com.charlotte.lab.jvm.memory.error;

/**
 * VM Args：-Xss2M 设置大容量的栈帧（这时候不妨设大些，请在32位系统下运行）
 * 不断创建新的永久线程，插入新的栈帧，导致虚拟机栈内存占用超出进程内存的最大值
 *
 * @author zzm
 */
@SuppressWarnings("不断创建线程导致Windows系统假死")
public class JavaVMStackOOM {
    private void dontStop() {
        while (true) {
        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) throws Throwable {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}