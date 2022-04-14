package cn.cidea.lab.leetcode;

/**
 * Created by Charlotte on2020/3/22
 */
public class DiStringMatch0942 {
    public static int[] diStringMatch(String S) {
        int n = S.length();
        int[] ans = new int[n + 1];
        int i = 0;
        int min = 0;
        int max = n;
        for (char c : S.toCharArray()) {
            if ('D' == c) {
                ans[i] = max--;
            } else {
                ans[i] = min++;
            }
            i++;
        }
        ans[i] = min++;
        return ans;
    }
}
