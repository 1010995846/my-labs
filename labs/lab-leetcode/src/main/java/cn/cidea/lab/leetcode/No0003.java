package cn.cidea.lab.leetcode;


/**
 * PASS
 * <p>
 * 无重复字符的最长子串
 * Longest Substring Without Repeating Characters
 */
public class No0003 {

    /**
     * Given a string, find the length of the longest substring without repeating characters.
     * <p>
     * Examples:
     * <p>
     * Given "abcabcbb", the answer is "abc", which the length is 3.
     * <p>
     * Given "bbbbb", the answer is "b", with the length of 1.
     * <p>
     * Given "pwwkew", the answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
     *
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(new No0003().lengthOfLongestSubstring("pwwkew"));
    }

    public int lengthOfLongestSubstring(String s) {
        String maxStr = "";
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!str.contains(String.valueOf(c))) {
                str += c;
                if (str.length() > maxStr.length()) {
                    maxStr = str;
                }
            } else {
                str = str.substring(str.lastIndexOf(c) + 1);
                str += c;
            }
        }
        return maxStr.length();
    }
}
