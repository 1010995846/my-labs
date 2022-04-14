package cn.cidea.lab.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charlotte on2020/3/22
 */
public class No1018_PrefixesDivBy5 {
    public static List<Boolean> prefixesDivBy5(int[] A) {
        List<Boolean> ans = new ArrayList<>();
        int n = 0;
        for (int i = 0; i < A.length; i++) {
            n = (n << 1) + A[i];
            ans.add(n % 5 == 0);
        }
        return ans;
    }

    public static void main(String[] args) {
        prefixesDivBy5(new int[]{1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1});
    }
}
