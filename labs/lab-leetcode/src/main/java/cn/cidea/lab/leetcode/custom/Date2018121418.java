package cn.cidea.lab.leetcode.custom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 运算解析
 */
public class Date2018121418 {

    private static ArrayList<String> preList;
    private static ArrayList<String> sufList;
    private static Set<String> optSet;

    static {
        preList = new ArrayList<>();
        preList.add("(");
        sufList = new ArrayList<>();
        sufList.add(")");
        optSet = new HashSet<>();
        optSet.add("+");
        optSet.add("-");
//        optSet.add("*");
//        optSet.add("/");
    }

    public static void main(String[] args) {

    }

    public static void excute(String str) {
        Stack<String> stack = new Stack<>();

        String num = "";
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                num += c;
            }
        }

    }

    private static void excuteSub(Stack<String> stack) {
    }


}
