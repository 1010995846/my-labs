package cn.cidea.lab.leetcode.cn;
//给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复
//的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 示例： 
//
// 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
//
//满足要求的三元组集合为：
//[
//  [-1, 0, 1],
//  [-1, -1, 2]
//]
// 
// Related Topics 数组 双指针 
// 👍 2517 👎 0

import java.util.*;

@Deprecated
class ThreeSum {
    public static void main(String[] args) {
        Solution solution = new ThreeSum().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            int target = 0;
            List<List<Integer>> list = new ArrayList<>();
            for (int i = 0; i < nums.length - 2; i++) {
                for (int j = i + 1; j < nums.length - 1; j++) {
                    for (int n = j + 1; n < nums.length; n++) {
                        if (target == (nums[i] + nums[j] + nums[n])) {
                            List<Integer> integerList = new ArrayList<>();
                            integerList.add(nums[i]);
                            integerList.add(nums[j]);
                            integerList.add(nums[n]);
                            Collections.sort(integerList);
                            if (!list.contains(integerList)) {
                                list.add(integerList);
                            }
                        }
                    }
                }
            }
            return list;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}