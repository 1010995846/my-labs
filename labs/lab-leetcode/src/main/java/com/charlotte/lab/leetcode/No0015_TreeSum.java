package com.charlotte.lab.leetcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class No0015_TreeSum {

//    给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。
//    注意：答案中不可以包含重复的三元组。
//    示例：
//    给定数组 nums = [-1, 0, 1, 2, -1, -4]，
//    满足要求的三元组集合为：
//            [
//            [-1, 0, 1],
//            [-1, -1, 2]
//            ]

    public static void main(String[] args) {
        int[] nums = new int[]{};

        // key: 两数之和，value,下标集合
        Map<Integer, Integer[]> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {

            }
        }
    }


}
