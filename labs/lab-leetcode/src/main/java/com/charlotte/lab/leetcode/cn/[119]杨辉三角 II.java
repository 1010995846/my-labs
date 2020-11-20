package com.charlotte.lab.leetcode.cn;
//给定一个非负索引 k，其中 k ≤ 33，返回杨辉三角的第 k 行。 
//
// 
//
// 在杨辉三角中，每个数是它左上方和右上方的数的和。 
//
// 示例: 
//
// 输入: 3
//输出: [1,3,3,1]
// 
//
// 进阶： 
//
// 你可以优化你的算法到 O(k) 空间复杂度吗？ 
// Related Topics 数组 
// 👍 171 👎 0

import java.util.ArrayList;
import java.util.List;

@Deprecated
class PascalsTriangleIi {
    public static void main(String[] args) {
        Solution solution = new PascalsTriangleIi().new Solution();
        List<Integer> row = solution.getRow(3);
        System.out.println(row);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<Integer> getRow(int rowIndex) {
            rowIndex++;
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < rowIndex; i++) {
                if (i == 0) {
                    list.add(1);
                } else {
                    long num = (long) list.get(i - 1) * (long) (rowIndex - i) / i;
                    list.add((int) num);
                }
            }
            return list;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}