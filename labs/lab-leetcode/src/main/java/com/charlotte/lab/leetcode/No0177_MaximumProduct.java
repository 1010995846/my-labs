package com.charlotte.lab.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charlotte on2020/3/21
 */
public class No0177_MaximumProduct {

    public static void main(String[] args) {
        System.out.println(maximumProduct(new int[]{1, 2, 3}));
    }


    public static int maximumProduct(int[] nums) {
        // 维护一个递增数组
        int[] arr = new int[]{nums[0], nums[1], nums[2]};
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int p = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = p;
                }
            }
        }
        for (int i = 3; i < nums.length; i++) {
            if (arr[0] < nums[i]) {
                for (int j = 1; j < arr.length; j++) {
                    if (arr[j] < nums[i]) {
                        arr[j - 1] = arr[j];
                        if (j == arr.length - 1) {
                            arr[j] = nums[i];
                        }
                    } else {
                        arr[j - 1] = nums[i];
                        break;
                    }
                }
            }
        }
        return arr[0] * arr[1] * arr[2];
    }
}
