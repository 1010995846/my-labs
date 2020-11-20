package com.charlotte.lab.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * PASS
 * <p>
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 */
public class No0001_TwoSum {


    public static int[] twoSum1(int[] nums, int target) {
        int[] indexs = new int[2];
        int a, b, c;
        for (int i = 0; i < nums.length; i++) {
            a = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                b = nums[j];
                if ((a + b) == target) {
                    indexs[0] = i;
                    indexs[1] = j;
                    return indexs;
                }
            }
        }
        return indexs;
    }

    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[]{i, map.get(complement)};
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public static int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        System.out.println(map);
    }
}
