package cn.cidea.lab.leetcode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * PASS
 * <p>
 * 完全平方数
 * 给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。你需要让组成和的完全平方数的个数最少。
 * Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.
 */
public class No0279_PerfectSquares {

    public static void main(String[] args) {
        System.out.println(numSquares(12));
        System.out.println(numSquares(13));
    }

    /**
     * 思路，BFS
     * 找出该数的平方根取整，并递减至1的集合，作为层级相加的广度延伸基数
     * 根为0，从集合中遍历取值平方相加向下一节点延伸
     *
     * @param n
     * @return
     */
    public static int numSquares(int n) {
        int maxRoot = (int) Math.sqrt(n);

        int round = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        Set<Integer> used = new HashSet<>();

        while (!queue.isEmpty()) {
            round++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int root = queue.poll();
                for (int num = maxRoot; num > 0; num--) {
                    int res = root + num * num;
                    if (res == n) {
                        return round;
                    }
                    if (used.contains(res)) {
                        continue;
                    }
                    used.add(res);
                    queue.add(res);
                }
            }
        }
        return -1;
    }

}
