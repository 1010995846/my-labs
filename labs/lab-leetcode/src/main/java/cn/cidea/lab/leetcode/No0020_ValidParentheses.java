package cn.cidea.lab.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * PASS
 */
public class No0020_ValidParentheses {

    public static void main(String[] args) {
        String s;

        s = "()";
        System.out.println(isValid(s));

        s = "()[]{}";
        System.out.println(isValid(s));

        s = "(]";
        System.out.println(isValid(s));

        s = "([)]";
        System.out.println(isValid(s));

        s = "{[]}";
        System.out.println(isValid(s));

    }


    private static Map<Character, Character> characterMap;

    static {
        characterMap = new HashMap<>();
        characterMap.put('(', ')');
        characterMap.put('{', '}');
        characterMap.put('[', ']');
    }

    /**
     * 消除思路，从相邻的配对括号开始消除
     * 当字符为后置时，false
     * 当字符为前置时，跳至下一字符，若为对应后置，则消去，若为后置且不对应，则false，若为前置，则继续判断
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {

        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (characterMap.containsKey(c)) {
                stack.push(c);
            } else {
//                空栈，即检索到后置括号且无前置
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if (characterMap.get(top) != c) {
                    return false;
                }
            }

        }
//        是否存在多余后置括号
        return stack.isEmpty();
    }

}
