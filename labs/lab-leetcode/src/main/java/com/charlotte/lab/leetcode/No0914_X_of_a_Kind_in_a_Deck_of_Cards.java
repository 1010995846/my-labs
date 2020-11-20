package com.charlotte.lab.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一副牌，每张牌上都写着一个整数。
 * <p>
 * 此时，你需要选定一个数字 X，使我们可以将整副牌按下述规则分成 1 组或更多组：
 * <p>
 * 每组都有 X 张牌。
 * 组内所有的牌上都写着相同的整数。
 * 仅当你可选的 X >= 2 时返回 true。
 */
public class No0914_X_of_a_Kind_in_a_Deck_of_Cards {

    public static void main(String[] args) {
        System.out.println(hasGroupsSizeX(new int[]{1, 2, 3, 4, 4, 3, 2, 1}));
        System.out.println(hasGroupsSizeX(new int[]{1, 1, 1, 2, 2, 3, 3}));
        System.out.println(hasGroupsSizeX(new int[]{1}));
        System.out.println(hasGroupsSizeX(new int[]{1, 1}));
        System.out.println(hasGroupsSizeX(new int[]{1, 1, 2, 2, 2, 2}));
    }


    public static boolean hasGroupsSizeX(int[] deck) {
        int maxNum = 0;
        /**
         * 统计数字个数
         */
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int d : deck) {
            Integer num = map.get(d);
            if (num == null) {
                num = new Integer(0);
            }
            num = num + 1;
            map.put(d, num);
            if (maxNum < num) {
                maxNum = num;
            }
        }

        /**
         * 检查所有数字个数是否都能被某个大于2的数整除
         */
        for (int i = 2; i <= maxNum; i++) {
            int sum = 0;
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                sum = sum + entry.getValue() % i;
            }
            if (sum == 0) {
                return true;
            }
        }
        return false;
    }

}