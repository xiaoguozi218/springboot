package com.example.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目描述：
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 *
 * 解法一： dfs
 *     1、当前左右括号都有大于 0 个可以使用的时候，才产生分支；
 *     2、产生左分支的时候，只看当前是否还有左括号可以使用；
 *     3、产生右分支的时候，还受到左分支的限制，右边剩余可以使用的括号数量一定得在严格大于左边剩余的数量的时候，才可以产生分支；
 *     4、在左边和右边剩余的括号数都等于 0 的时候结算。
 */
public class Code22_括号生成 {

    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        dfs(result, "", n , n);
        return result;
    }

    public static void dfs(List<String> result, String curStr, int left, int right) {
        if (left == 0 && right == 0) {
            result.add(curStr);
            return;
        }
        if (left > 0) {
            dfs(result, curStr + "(", left -1 ,right);
        }
        if (right > left) {
            dfs(result, curStr + ")", left ,right-1);
        }
    }

    public static void main(String[] args) {
        generateParenthesis(3);
    }
}
