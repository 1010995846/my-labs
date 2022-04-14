package cn.cidea.lab.leetcode.custom;

import java.util.*;

/**
 * 运算解析
 */
public class Date2018121416 {

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
        int loc = -1;
        boolean excute = false;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((loc = preList.indexOf(c)) >= 0) {
                stack.push(String.valueOf(c));
                continue;
            } else if ((loc = sufList.indexOf(c)) >= 0) {
//                后括号
                String pre = stack.peek();
                if (preList.contains(pre) || sufList.contains(pre)) {
//                    前括号
                    if (pre.equals(preList.get(loc))) {
                        throw new RuntimeException("括号不匹配");
                    }
                } else {

                }
//                if(pre.equals(preList.get(loc))){
////                    对位括号，出栈
//                    stack.pop();
//                    continue;
//                }
//                    stack.pop();
//                    continue;
//                }
//                stack.push(num);
//                excuteSub(stack);
//                num = stack.pop();
//                if()
//                String pre = stack.pop();
//                if(pre.equals(preList.get(loc))){
//                    throw new RuntimeException("括号不匹配");
//                }
//                stack.push(num);
//                continue;
            } else if (optSet.contains(c)) {
                stack.push(num);
//                excuteSub(stack);
                stack.push(String.valueOf(c));
                continue;
            }
            if (Character.isDigit(c)) {
                throw new RuntimeException("非法数字");
            }
            num += c;
        }
    }

    private static void excuteSub(Stack<String> stack) {
        int b = Integer.valueOf(stack.pop());
        int optr = Integer.valueOf(stack.pop());
        int a = Integer.valueOf(stack.pop());
        if ("+".equals(optr)) {
            stack.push(String.valueOf(a + b));
        } else if ("-".equals(optr)) {
            stack.push(String.valueOf(a - b));
        } else {
            throw new RuntimeException("未识别运算符");
        }
    }


}
