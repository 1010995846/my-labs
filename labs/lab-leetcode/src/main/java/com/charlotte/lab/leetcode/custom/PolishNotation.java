package com.charlotte.lab.leetcode.custom;

import java.util.*;

/**
 * 前缀、中缀、后缀表达式
 */
public class PolishNotation {

    private static List<String> preList;
    private static List<String> sufList;
    private static Map<String, Integer> optMap;

    static {
        preList = new ArrayList<>();
        preList.add("(");
        sufList = new ArrayList<>();
        sufList.add(")");
        optMap = new HashMap<>();
        optMap.put("+", 1);
        optMap.put("-", 1);
        optMap.put("*", 2);
        optMap.put("/", 2);
    }

    public static void main(String[] args) {
        String s = "";
        s = "2*2+((2+3)*4)-5*4";//-+*22*+234*54     //22*23+4*+54*-
//        s = "1*2+3+4*5-6*7";//++*123-*45*67     //12*3+45*+47*-

//        System.out.println(positive(s));
        /**
         * 2*3+4*5  23*45*+
         * 2+3-4-5  23+4-5-
         * 2+3-4-5*6  23+4-56*-
         * 2+3-4-5*6*7  23+4-56*7*-
         */
//        System.out.println(reverse("2*3+4*5"));
//        System.out.println(reverse("2+3-4-5"));
//        System.out.println(reverse("2+3-4-5*6"));
//        System.out.println(reverse("2+3-4-5*6*7"));
        System.out.println(reverse("1/5+((2*7+3*4-8-9)*4)-5*6"));
    }

    /**
     * 转前缀表达式
     * 前缀表达式运算规则，从右至左，先扫先算
     *
     * @param s
     * @return
     */
    public static String positive(String s) {
        String num = "";
        Stack<String> stack = new Stack<>();
        Stack<String> optStack = new Stack<>();
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
//            System.out.println(c);
            if (Character.isDigit(c) || i == 0) {
                num = c + num;
                if (i == 0) {
                    stack.push(num);
                }
            } else {
//                非数字
                String ch = String.valueOf(c);
                if (!"".equals(num)) {
                    stack.push(num);
                    num = "";
                }
                int at;
                if (optMap.keySet().contains(ch)) {
//                    运算符
                    if (optStack.isEmpty() || sufList.contains(optStack.peek())) {
                        optStack.push(ch);
                    } else {
                        String opt = null;
                        while (!optStack.isEmpty() && optMap.keySet().contains(opt = optStack.peek())
                                && optMap.get(ch) < optMap.get(opt)) {
//                            遇到低位运算符
                            optStack.pop();
                            stack.push(opt);
                        }
                        optStack.push(ch);
                    }
                } else if ((at = preList.indexOf(ch)) >= 0) {
//                    开
                    try {
                        String opt = null;
                        while (!sufList.get(at).equals(opt = optStack.pop())) {
                            stack.push(opt);
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("表达式不合法！");
                    }
                } else if ((at = sufList.indexOf(ch)) >= 0) {
//                    闭
                    optStack.push(ch);
                } else {
                    throw new IllegalArgumentException("非法字符 " + ch + " ！");
                }
            }
        }
        while (!optStack.isEmpty()) {
            stack.push(optStack.pop());
        }
        s = "";
        while (!stack.isEmpty()) {
            s += stack.pop();
        }
        return s;
    }

    /**
     * 后缀表达式规则，从左至右，先扫先算
     * 栈先入先出，因此标准表达式从左至右扫描时，
     * 若当前运算符较前一个（栈顶）低位（低位低优先）或同位（运算和扫描从左至右，先入优先），则前一个运算符压入后缀表达式，当前运算符入栈继续扫描比较
     * 若当前运算符较前一个高位，入栈继续扫描比较
     * 扫描结束时，若栈不为空，此时栈内运算符由高位至低位且不存在平级，依次出栈压入表达式
     * 数字，入s栈
     * 运算符ch：o栈为空，入o栈；o栈非空，若栈顶为运算符opt：若ch比opt低位或同位，则不影响顺位，o栈出入s，ch入o栈，再次比较；若高位，入o栈
     * o栈剩余顺位入s栈
     *
     * @param s
     * @return
     */
    public static String reverse(String s) {
        String num = "";
        Stack<String> stack = new Stack<>();
        Stack<String> optStack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || i == 0) {
                num += c;
                if (i == s.length() - 1) {
                    stack.push(num);
                }
            } else {
//                非数字
                String ch = String.valueOf(c);
                if (!"".equals(num)) {
                    stack.push(num);
                    num = "";
                }
                int at;
                if (optMap.keySet().contains(ch)) {
//                    运算符
                    if (optStack.isEmpty() || sufList.contains(optStack.peek())) {
                        optStack.push(ch);
                    } else {
                        String opt = null;
                        while (!optStack.isEmpty() && optMap.keySet().contains(opt = optStack.peek())
                                && optMap.get(ch) <= optMap.get(opt)) {
//                          遇到低或同位运算符，不影响顺位，出栈入后缀式
                            optStack.pop();
                            stack.push(opt);
                        }
                        optStack.push(ch);
                    }
                } else if ((at = preList.indexOf(ch)) >= 0) {
//                    开
                    optStack.push(ch);
                } else if ((at = sufList.indexOf(ch)) >= 0) {
//                    闭
                    try {
                        String opt = null;
                        while (!preList.get(at).equals(opt = optStack.pop())) {
//                            括号内表达式结束。直到弹出对应开括号，将剩余运算符压入（此时栈内若存在操作符，则按照出栈顺序：高位->低位），
                            stack.push(opt);
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("表达式不合法！");
                    }
                } else {
                    throw new IllegalArgumentException("非法字符 " + ch + " ！");
                }
            }

        }
        while (!optStack.isEmpty()) {
            stack.push(optStack.pop());
        }
        s = "";
        while (!stack.isEmpty()) {
            s = stack.pop() + s;
        }
        return s;
    }


}
