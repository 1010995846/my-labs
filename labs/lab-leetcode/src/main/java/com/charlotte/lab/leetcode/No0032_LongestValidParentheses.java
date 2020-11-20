package com.charlotte.lab.leetcode;

import java.util.Stack;

/**
 * Created by Charlotte on2020/3/25
 * 给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。
 * 临近匹配，采用栈解决
 */
public class No0032_LongestValidParentheses {
    public static int longestValidParentheses(String s) {
        int max = 0;
        Stack<Character> stack = new Stack<>();
        int r = 0;
        for (char c : s.toCharArray()) {
            if ('(' == c) {
                if (!stack.isEmpty() && stack.peek() != '(') {
//                    断连，重新开始，并清空栈
                    stack.clear();
                    max = Math.max(max, r);
                    r = 0;
                }
                stack.push(c);
            } else if (')' == c) {
                if (stack.isEmpty() || '(' != stack.pop()) {
//                    无效匹配，重新开始，并清空栈
                    stack.clear();
                    max = Math.max(max, r);
                    r = 0;
                    continue;
                }
//                有效
                r += 2;
            }
        }
        max = Math.max(max, r);
        return max;
    }

    public static void main(String[] args) {
        System.out.println(longestValidParentheses("()(()"));
    }
}
