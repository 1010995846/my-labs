package com.charlotte.lab.leetcode.cn;

public class ListNode {

    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }


    public static ListNode get(int[] nums) {
        ListNode head = null;
        ListNode pre = null;
        for (int i = 0; i < nums.length; i++) {
            ListNode node = new ListNode(nums[i]);
            if (head == null) {
                head = node;
            }
            if (pre != null) {
                pre.next = node;
            }
            pre = node;
        }
        return head;
    }

    public static ListNode get(int n) {
        return get(n, 0);
    }

    public static ListNode get(int n, int correction) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i + 1 + correction;
        }
        return get(nums);
    }
}
