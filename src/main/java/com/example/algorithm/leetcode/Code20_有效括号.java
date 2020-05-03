package com.example.algorithm.leetcode;

import org.apache.commons.lang3.StringUtils;

import java.util.Stack;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
 *
 * 有效字符串需满足：
 *
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 * 注意空字符串可被认为是有效字符串。
 *
 * 链接：https://leetcode-cn.com/problems/valid-parentheses
 */
public class Code20_有效括号 {

    /**
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        Stack<Character> stack = new Stack<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c) return false;
        }
        return stack.isEmpty();
    }

    /**
     * 使用字符串替换和递归的方式，写起来极简~
     * @param s
     * @return
     */
    public static boolean isValid2(String s) {
        if (s.contains("()") || s.contains("[]") || s.contains("{}")) {
            return isValid2(s.replace("()", "").replace("[]", "").replace("{}", ""));
        } else {
            return "".equals(s);
        }
    }

    public static void main(String[] args) {
        boolean valid = isValid2("(){}[");
        System.err.println(valid);
    }
}
