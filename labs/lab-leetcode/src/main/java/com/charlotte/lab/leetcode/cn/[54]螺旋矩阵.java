package com.charlotte.lab.leetcode.cn;//给定一个包含 m x n 个元素的矩阵（m 行, n 列），请按照顺时针螺旋顺序，返回矩阵中的所有元素。
//
// 示例 1: 
//
// 输入:
//[
// [ 1, 2, 3 ],
// [ 4, 5, 6 ],
// [ 7, 8, 9 ]
//]
//输出: [1,2,3,6,9,8,7,4,5]
// 
//
// 示例 2: 
//
// 输入:
//[
//  [1, 2, 3, 4],
//  [5, 6, 7, 8],
//  [9,10,11,12]
//]
//输出: [1,2,3,4,8,12,11,10,9,5,6,7]
// 
// Related Topics 数组 
// 👍 466 👎 0


import java.util.ArrayList;
import java.util.List;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {

    public static void main(String[] args) {
        new Solution().spiralOrder(new int[][]{{1}});
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> list = new ArrayList<>();
        if (matrix.length == 0) {
            return list;
        }
        boolean[][] bit = new boolean[matrix.length][matrix[0].length];
        // 状态 * 4，控制方向
        // 线性状态，可能状态 * 2也可
        boolean toRight = true;
        boolean toDown = true;

        int r = 0;
        int c = 0;
        while (true) {
            if (c < 0 || r < 0 || c >= matrix[0].length || r >= matrix.length || bit[r][c]) {
                // 无可移动
                return list;
            }
            list.add(matrix[r][c]);
            bit[r][c] = true;

            // 移动位置
            if (toRight && toDown) {
                // 右移
                if (c >= matrix[0].length - 1 || bit[r][c + 1]) {
                    // 边界或下一域已访问
                    toRight = !toRight;
                    r++;
                } else {
                    c++;
                }
            } else if (!toRight && toDown) {
                // 下移
                if (r >= matrix.length - 1 || bit[r + 1][c]) {
                    toDown = !toDown;
                    c--;
                } else {
                    r++;
                }
            } else if (!toRight && !toDown) {
                // 左移
                if (c == 0 || bit[r][c - 1]) {
                    toRight = !toRight;
                    r--;
                } else {
                    c--;
                }
            } else if (toRight && !toDown) {
                // 上移
                if (r == 0 || bit[r - 1][c]) {
                    toDown = !toDown;
                    c++;
                } else {
                    r--;
                }
            }
        }

    }
}
//leetcode submit region end(Prohibit modification and deletion)
