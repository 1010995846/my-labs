package com.charlotte.lab.leetcode.cn;
//给定一个由整数组成的非空数组所表示的非负整数，在该数的基础上加一。 
//
// 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。 
//
// 你可以假设除了整数 0 之外，这个整数不会以零开头。 
//
// 示例 1: 
//
// 输入: [1,2,3]
//输出: [1,2,4]
//解释: 输入数组表示数字 123。
// 
//
// 示例 2: 
//
// 输入: [4,3,2,1]
//输出: [4,3,2,2]
//解释: 输入数组表示数字 4321。
// 
// Related Topics 数组 
// 👍 528 👎 0

class PlusOne {
    public static void main(String[] args) {
        Solution solution = new PlusOne().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] plusOne(int[] digits) {

            for (int i = digits.length - 1; i >= 0; i--) {
                int plus = digits[i] + 1;
                if (plus >= 10) {
                    digits[i] = plus % 10;
                } else {
                    digits[i] = plus;
                    break;
                }
            }
            if (digits[0] == 0) {
                int[] plusDigits = new int[digits.length + 1];
                plusDigits[0] = 1;
                for (int i = 0; i < digits.length; i++) {
                    plusDigits[i + 1] = digits[i];
                }
                return plusDigits;
            }
            return digits;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}