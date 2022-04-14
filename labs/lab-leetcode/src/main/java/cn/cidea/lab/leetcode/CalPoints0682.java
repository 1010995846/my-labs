package cn.cidea.lab.leetcode;

/**
 * Created by Charlotte on2020/3/22
 */
public class CalPoints0682 {
    public int calPoints(String[] ops) {
        // 数组模拟栈操作
        int[] scores = new int[ops.length];
        int ans = 0;
        int p = 0;
        for (int i = 0; i < ops.length; i++) {
            Integer integer = 0;
            String op = ops[i];
            if ("+".equals(op)) {
                integer += Integer.valueOf(scores[p - 1]) + Integer.valueOf(scores[p - 2]);
                scores[p++] = integer;
            } else if ("D".equals(op)) {
                integer = Integer.valueOf(scores[p - 1]) * 2;
                scores[p++] = integer;
            } else if ("C".equals(op)) {
                integer = -Integer.valueOf(scores[p - 1]);
                scores[--p] = integer;
            } else {
                integer = Integer.valueOf(op);
                scores[p++] = integer;
            }
            ans += integer;
        }
        return ans;
    }


    public static void main(String[] args) {
        String[] strings = {"-60", "D", "-36", "30", "13", "C", "C", "-33", "53", "79"};
        new CalPoints0682().calPoints(strings);
    }
}
