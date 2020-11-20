package com.charlotte.lab.leetcode;

import java.util.Stack;

/**
 * PASS
 */
public class No0150_EvaluateReversePolishNotation {

    public static void main(String[] args) {
        No0150_EvaluateReversePolishNotation no0150 = new No0150_EvaluateReversePolishNotation();
        System.out.println(no0150.evalRPN(new String[]{"2", "1", "+", "3", "*"}));
        System.out.println(no0150.evalRPN(new String[]{"4", "13", "5", "/", "+"}));
        System.out.println(no0150.evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}));

    }

    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {
            if (!token.equals("+") && !token.equals("-") && !token.equals("*") && !token.equals("/")) {
//                数字
                stack.push(Integer.valueOf(token));
            } else {
//                运算符
                int b = stack.pop();
                int a = stack.pop();
                int res;
                if ("+".equals(token)) {
                    stack.push(a + b);
                } else if ("-".equals(token)) {
                    stack.push(a - b);
                } else if ("*".equals(token)) {
                    stack.push(a * b);
                } else if ("/".equals(token)) {
                    stack.push(a / b);
                }
            }
        }
        return stack.pop();
    }
}
