package com.charlotte.lab.leetcode;

import java.util.*;

/**
 * You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'. The wheels can rotate freely and wrap around: for example we can turn '9' to be '0', or '0' to be '9'. Each move consists of turning one wheel one slot.
 * <p>
 * The lock initially starts at '0000', a string representing the state of the 4 wheels.
 * <p>
 * You are given a list of deadends dead ends, meaning if the lock displays any of these codes, the wheels of the lock will stop turning and you will be unable to open it.
 * <p>
 * Given a target representing the value of the wheels that will unlock the lock, return the minimum total number of turns required to open the lock, or -1 if it is impossible.
 */
public class No0752_OpenTheLock {

    public static void main(String[] args) {
        No0752_OpenTheLock test = new No0752_OpenTheLock();
        String[] deadends;
        String targer;

        deadends = new String[]{"0201", "0101", "0102", "1212", "2002"};
        targer = "0202";
        System.out.println(test.openLock(deadends, targer));

        deadends = new String[]{"8888"};
        targer = "0009";
        System.out.println(test.openLock(deadends, targer));

        deadends = new String[]{"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"};
        targer = "8888";
        System.out.println(test.openLock(deadends, targer));

        deadends = new String[]{"0000"};
        targer = "8888";
        System.out.println(test.openLock(deadends, targer));

//        char c = 5;
//        System.out.println(c);
//        int i = '1';
//        System.out.println(Integer.valueOf('2') - 48);
    }


    public int openLock(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        int round = 0;
        String initCode = "0000";

        String s = String.valueOf('a');

        if (initCode.equals(target)) {
            return round;
        }
        if (deadSet.contains(initCode)) {
            return -1;
        }

        Queue<String> queue = new LinkedList<>();
        queue.add(initCode);
        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
//          遍历下一节点，并对每次遍历BFS，添加进队列中
            int size = queue.size();
            round++;
//            遍历当前层级
            for (int n = 0; n < size; n++) {
//                FIFO
                char[] lock = queue.poll().toCharArray();
                /**
                 * BFS
                 */
//                逐位拨动锁位
                for (int i = 0; i < lock.length; i++) {
                    int b = 1;
//                    单锁位上下拨动各一次
                    for (int j = 0; j < 2; j++) {
                        char[] clone = lock.clone();
                        b = 0 - b;
                        clone[i] = (char) ((Integer.valueOf(clone[i]) - 48 + 10 + b) % 10 + 48);
//                        拨动后的密码
                        String cloneStr = String.valueOf(clone);
                        /**
                         * 方案一：搜索完立即检查，但需要对初始值进行检查；
                         * 方案二：遍历开始时即进行检查，可能造成多余运算。
                         */
                        if (deadSet.contains(cloneStr)) {
                            continue;
                        }
                        if (target.equals(cloneStr)) {
                            return round;
                        }
                        if (visited.contains(cloneStr)) {
                            continue;
                        }
                        queue.add(cloneStr);
                        visited.add(cloneStr);
                    }
                }
            }
        }

        return -1;
    }


}
