package com.charlotte.lab.leetcode;

import javafx.scene.control.TableCell;

import java.util.*;

/**
 * You are given a list of non-negative integers, a1, a2, ..., an, and a target, S. Now you have 2 symbols + and -.
 * For each integer, you should choose one from + and - as its new symbol.
 * Find out how many ways to assign symbols to make sum of integers equal to target S.
 */
public class No0494_TargetSum {

    public static void main(String[] args) {

        int[] nums;
        int S;

//        nums = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
//        S = 1;
//        System.out.println(findTargetSumWaysBFS1(nums, S));

        nums = new int[]{1, 1, 1, 1, 1};
        S = 1;
//        System.out.println(findTargetSumWaysBFS1(nums, S));
        System.out.println(findTargetSumWaysDFS1(nums, S));

    }


    public static int findTargetSumWaysDFS1(int[] nums, int S) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int sum = findTargetSumWaysDFS2(nums, 0, S, 0, 0);
        return sum;
    }

    /**
     * @param nums    序列
     * @param i       序列号
     * @param S       目标
     * @param account 栈，节点值
     * @param sum     数量
     * @return
     */
    public static int findTargetSumWaysDFS2(int[] nums, int i, int S, int account, int sum) {

        char[] chars = new char[]{'+', '-'};

        for (char c : chars) {
//            下一节点值
            int next;
            if (c == '+') {
                next = account + nums[i];
            } else if (c == '-') {
                next = account - nums[i];
            } else {
                throw new IllegalArgumentException("char method not defined!");
            }
            if (i == (nums.length - 1)) {
//                尾节点
                if (S == next) {
                    sum++;
                }
                continue;
            }
            sum = findTargetSumWaysDFS2(nums, i + 1, S, next, sum);
        }

        return sum;
    }

//    public static int findTargetSumWaysBFS1(int[] nums, int S) {
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(0);
//        Queue<Integer> curQueue = new LinkedList<>();
//        Queue<Integer> nextQueue = new LinkedList<>();
//        Map<Integer, Integer> used = new HashMap<>();
//        Map<Integer, Integer> nextUsed = new HashMap<>();
//        Map<Integer, Set<Integer>> unionMap = new HashMap<>();
//        used.put(0, 1);
//
//        for (int i = 0; i < nums.length; i++) {
//            int size = queue.size();
//            for (int j = 0; j < size; j++){
//                int cur = queue.poll();
//                findTargetSumWaysBFS2(queue, unionMap, nextUsed, cur, cur + nums[i]);
//                findTargetSumWaysBFS2(queue, unionMap, nextUsed, cur, cur - nums[i]);
//            }
//            System.out.println();
//            for (Map.Entry<Integer, Set<Integer>> entry : unionMap.entrySet()){
//                int cur = entry.getKey();
//                int curSum = used.get(cur);
//                for (Integer next : entry.getValue()){
//                    int nextSum = nextUsed.get(next);
//                    nextUsed.put(next, curSum * nextSum);
//                }
//            }
//            used = nextUsed;
//            nextUsed = new HashMap<>();
//            unionMap = new HashMap<>();
//        }
//
//        Integer sum;
//        if((sum = nextUsed.get(S)) == null){
//            return 0;
//        }
//        return sum;
//    }
//
//
//    public static void findTargetSumWaysBFS2(Queue<Integer> queue, Map<Integer, Set<Integer>> unionMap, Map<Integer, Integer> nextUsed, int cur, int next) {
//
//        Integer nextSum;
//        if((nextSum = nextUsed.get(next)) == null){
//            Set<Integer> set = unionMap.get(cur);
//            if(set == null){
//                set = new HashSet<>();
//            }
//            set.add(next);
//            unionMap.put(cur, set);
//            queue.add(next);
//            nextSum = 0;
//        }
//        nextSum++;
//        nextUsed.put(next, nextSum);
//    }

}
