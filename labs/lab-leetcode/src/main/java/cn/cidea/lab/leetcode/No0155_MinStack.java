package cn.cidea.lab.leetcode;

import java.util.Stack;

/**
 * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 * <p>
 * push(x) -- Push element x onto stack.
 * pop() -- Removes the element on top of the stack.
 * top() -- Get the top element.
 * getMin() -- Retrieve the minimum element in the stack.
 */
public class No0155_MinStack {

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(-2);
        System.out.println(minStack.getMin());
        minStack.push(0);
        System.out.println(minStack.getMin());
        minStack.push(3);
        System.out.println(minStack.getMin());
        minStack.push(-2);
        minStack.pop();
        minStack.pop();
        System.out.println(minStack.getMin());
    }

    static class MinStack {

        Stack<Integer> stack;

        /**
         * 存放栈内最小值
         * 当入栈x，若x小于等于最小栈顶部top，则x为栈最小值（之一），入最小栈
         * 当出栈时，若出栈top等于最小栈top，即出栈值为最小值（之一），最小栈跟随出栈，出栈后top即为原top入栈前的最小值
         */
        Stack<Integer> minStack;

        /**
         * initialize your data structure here.
         */
        public MinStack() {
            stack = new Stack<>();
            minStack = new Stack<>();
        }

        public void push(int x) {
            stack.push(x);

            int minNum;
            if (minStack.isEmpty()) {
                minNum = x;
            } else {
                minNum = minStack.peek();
            }
            if (x <= minNum) {
                minStack.push(x);
            }
        }

        public void pop() {
            int num = stack.pop();

            int minNum = minStack.peek();
            if (num == minNum) {
                minStack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    /**
     * Your MinStack object will be instantiated and called as such:
     * MinStack obj = new MinStack();
     * obj.push(x);
     * obj.pop();
     * int param_3 = obj.top();
     * int param_4 = obj.getMin();
     */

}
