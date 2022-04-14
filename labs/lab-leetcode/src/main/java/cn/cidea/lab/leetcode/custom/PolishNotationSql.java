package cn.cidea.lab.leetcode.custom;

import java.util.*;

/**
 * 类get参数（电网框架查询参数格式）转sql条件
 */
public class PolishNotationSql {

    //    开括号
    private static List<String> preList;
    //    闭括号
    private static List<String> sufList;
    //    操作符集合和运算优先级
    private static Map<String, Integer> optMap;
    //    操作符集合和对应sql逻辑符
    private static Map<String, String> optSqlMap;

    static {
        preList = new ArrayList<>();
        preList.add("(");
        sufList = new ArrayList<>();
        sufList.add(")");
        optMap = new HashMap<>();
        optMap.put("&", 1);
        optMap.put("|", 1);
        optSqlMap = new HashMap<>();
        optSqlMap.put("&", "and");
        optSqlMap.put("|", "or");
    }

    public static void main(String[] args) {
//        System.out.println(reverse("21&(2|3)&8|(4&5)"));
        Stack<String> result = new Stack<>();

        Stack<String> stack = null;
        stack = reverse("id=1&model=2|code=3&(weight=80|weight=90)&type=8");
        System.out.println(stack);
        String where = "";
        while (!stack.isEmpty()) {
            String filter = stack.pop();
            if (optMap.keySet().contains(filter)) {
//				逻辑符号
                String re = result.pop();
                String le = result.pop();
                if (optSqlMap.get(filter) == null) {
                    throw new RuntimeException("非法逻辑符！");
                }
                result.push("(" + le + " " + optSqlMap.get(filter) + " " + re + ")");
            } else {
                result.push(filter);
            }
        }
        where = result.pop();
        if (!result.isEmpty()) {
            throw new RuntimeException("filter不合法！");
        }
        System.out.println(where);
    }


    public static Stack<String> reverse(String filter) {
        Stack<String> result = new Stack<>();
        if (filter == null) {
            return result;
        }
        String str = "";
        Stack<String> stack = new Stack<>();
        Stack<String> optStack = new Stack<>();
        for (int i = 0; i < filter.length(); i++) {
            char c = filter.charAt(i);
            String ch = String.valueOf(c);
            int loc;
            if (optMap.keySet().contains(ch)) {
//              逻辑符
                if (!"".equals(str)) {
                    stack.push(str);
                    str = "";
                }
                if (optStack.isEmpty() || sufList.contains(optStack.peek())) {
                    optStack.push(ch);
                } else {
                    String opt = null;
                    while (!optStack.isEmpty() && optMap.keySet().contains(opt = optStack.peek())
                            && optMap.get(ch) <= optMap.get(opt)) {
//                      遇到低或同位运算符，不影响顺位，出栈入式子
                        optStack.pop();
                        stack.push(opt);
                    }
                    optStack.push(ch);
                }
            } else if (preList.contains(ch)) {
                if (!"".equals(str)) {
                    stack.push(str);
                    str = "";
                }
                optStack.push(ch);
            } else if ((loc = sufList.indexOf(ch)) >= 0) {
                if (!"".equals(str)) {
                    stack.push(str);
                    str = "";
                }
                String opt = null;
                while (!preList.get(loc).equals(opt = optStack.pop())) {
                    stack.push(opt);
                }
            } else {
                str += c;
                if (i == filter.length() - 1) {
                    stack.push(str);
                }
            }
        }
        while (!optStack.isEmpty()) {
            result.push(optStack.pop());
        }
        while (!stack.isEmpty()) {
            result.push(stack.pop());
        }
        return result;
    }

}
