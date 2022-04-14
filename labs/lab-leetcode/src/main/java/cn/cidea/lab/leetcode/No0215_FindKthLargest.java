package cn.cidea.lab.leetcode;

import java.util.*;

/**
 * Created by Charlotte on2020/3/26
 * 在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 */
public class No0215_FindKthLargest {


    /**
     * 堆
     *
     * @param nums
     * @param k
     * @return
     */
    public static int findKthLargest(int[] nums, int k) {
//        一、使用栈实现堆，从大到小的栈，Stack继承Vector，内部是数组实现，使用链表数据结构进行优化
//        Stack<Integer> stack = new Stack<>();
//        Stack<Integer> gc = new Stack<>();
//        for (int i = 0; i < nums.length; i++) {
//            if(stack.isEmpty()){
//                stack.push(nums[i]);
//                continue;
//            }
//            while (!stack.isEmpty() && stack.peek() < nums[i]){
////                更大
//                gc.push(stack.pop());
//            }
//            stack.push(nums[i]);
//            while (!gc.isEmpty()){
//                stack.push(gc.pop());
//            }
//            if(stack.size() > k){
//                stack.pop();
//            }
//        }
//        return stack.peek();
//        二、使用链表实现堆
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!list.isEmpty() && list.getLast() < nums[i]) {
//                更大
                int j = 0;
                for (Integer integer : list) {
                    if (integer < nums[i]) {
                        list.add(j, nums[i]);
                        break;
                    }
                    j++;
                }
                if (list.size() > k) {
                    list.pollLast();
                }
            } else if (list.size() < k) {
                list.add(nums[i]);
            }
        }
        return list.peekLast();
    }

    public static void main(String[] args) {
        System.out.println(findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }

}
