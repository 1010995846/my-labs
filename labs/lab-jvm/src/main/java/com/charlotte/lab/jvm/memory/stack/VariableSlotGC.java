package com.charlotte.lab.jvm.memory.stack;

/**
 * 局部变量表变量槽的回收
 * 变量离开作用域后，变量槽被复用
 * 导致变量槽的原引用关系消失并能够回收
 * -verbose:gc
 * @author Charlotte
 */
public class VariableSlotGC {

    public static void main(String[] args) {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
//            placeholder = null;
        }
//        虽然离开变量的作用域，但栈帧中变量槽依旧保持着对对象的引用，导致gc无效
//        加入该行后才有效gc，变量槽被复用，引用消失
//        同理，将变量placeholder指向null也可成功gc
//        int a = 0;
        System.gc();
    }


}
