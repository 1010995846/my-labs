package cn.cidea.lab.leetcode;


import java.util.Stack;

/**
 * Given a list of daily temperatures, produce a list that, for each day in the input,
 * tells you how many days you would have to wait until a warmer temperature.
 * If there is no future day for which this is possible, put 0 instead.
 * <p>
 * For example, given the list temperatures = [73, 74, 75, 71, 69, 72, 76, 73], your output should be [1, 1, 4, 2, 1, 1, 0, 0].
 * <p>
 * Note: The length of temperatures will be in the range [1, 30000]. Each temperature will be an integer in the range [30, 100].
 */
public class No0783_DailyTemperatures {

    public static void main(String[] args) {
        No0783_DailyTemperatures no0783 = new No0783_DailyTemperatures();
        System.out.println(no0783.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));


    }


    /**
     * 遍历温度
     * 栈为空，入栈
     * 不为空，判断top
     * 小于等于top，入栈
     * 大于top，出栈，并设置top索引（？）天数，再次取top判断
     *
     * @param temperatures
     * @return
     */
    public int[] dailyTemperatures(int[] temperatures) {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> indexStack = new Stack<>();
        int[] days = new int[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            int temperature = temperatures[i];
            int top;
            while (!stack.isEmpty() && ((top = stack.peek()) < temperature)) {
//                    遇上升温，出栈，设置天数，再次判断栈顶是否升温
                stack.pop();
                int index = indexStack.pop();
                days[index] = i - index;
            }
            stack.push(temperature);
            indexStack.push(i);
        }
        return days;
    }
}
