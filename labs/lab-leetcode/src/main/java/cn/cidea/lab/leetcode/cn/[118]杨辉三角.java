package cn.cidea.lab.leetcode.cn;
//给定一个非负整数 numRows，生成杨辉三角的前 numRows 行。 
//
// 
//
// 在杨辉三角中，每个数是它左上方和右上方的数的和。 
//
// 示例: 
//
// 输入: 5
//输出:
//[
//     [1],
//    [1,1],
//   [1,2,1],
//  [1,3,3,1],
// [1,4,6,4,1]
//] 
// Related Topics 数组 
// 👍 339 👎 0

import java.util.ArrayList;
import java.util.List;

class PascalsTriangle {
    public static void main(String[] args) {
        Solution solution = new PascalsTriangle().new Solution();
        List<List<Integer>> triangle = solution.generate(5);
        System.out.println(triangle);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> generate(int numRows) {
            List<List<Integer>> triangle = new ArrayList<>(numRows);
            List<Integer> pre = null;
            for (int r = 0; r < numRows; r++) {
                List<Integer> row = new ArrayList<>(r + 1);
                triangle.add(row);
                for (int c = 0; c < r + 1; c++) {
                    if (c == 0 || c == r) {
                        row.add(1);
                    } else {
                        row.add(pre.get(c - 1) + pre.get(c));
                    }
                }
                pre = row;
            }
            return triangle;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}