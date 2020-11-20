package com.charlotte.lab.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charlotte on2020/3/22
 */
public class RestoreIpAddresses0093 {


    public List<String> restoreIpAddresses(String s) {
        List<String> ans = new ArrayList<>();
        // 一、暴力解
        char[] chars = s.toCharArray();
        for (int i = 1; i < 4 && i <= s.length(); i++) {
            String ip1 = s.substring(0, i);
            if (!isIpPart(ip1)) {
                continue;
            }
            for (int j = i + 1; j < i + 4 && j <= s.length(); j++) {
                String ip2 = s.substring(i, j);
                if (!isIpPart(ip2)) {
                    continue;
                }
                ip2 = ip1 + "." + ip2;
                for (int k = j + 1; k < j + 4 && k <= s.length(); k++) {
                    String ip3 = s.substring(j, k);
                    if (!isIpPart(ip3)) {
                        continue;
                    }
                    ip3 = ip2 + "." + ip3;
                    int l = s.length() - k;
                    if (l <= 3) {
                        String ip4 = s.substring(k, s.length());
                        if (!isIpPart(ip4)) {
                            continue;
                        }
                        ip4 = ip3 + "." + ip4;
                        ans.add(ip4);
                    }
                }
            }
        }
        return ans;
    }

    public boolean isIpPart(String s) {
        Integer integer = null;
        try {
            integer = Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return false;
        }
        if (0 <= integer && integer <= 255) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RestoreIpAddresses0093 ins = new RestoreIpAddresses0093();
        String s = "0000";
        ins.restoreIpAddresses(s);
    }
}
