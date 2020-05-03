package com.example.algorithm.leetcode;

/**
 * 实现 pow(x, n) ，即计算 x 的 n 次幂函数。
 *
 * 示例 1:
 *
 * 输入: 2.00000, 10
 * 输出: 1024.00000
 * 示例 2:
 *
 * 输入: 2.10000, 3
 * 输出: 9.26100
 * 示例 3:
 *
 * 输入: 2.00000, -2
 * 输出: 0.25000
 * 解释: 2-2 = 1/22 = 1/4 = 0.25
 * 说明:
 *
 * -100.0 < x < 100.0
 * n 是 32 位有符号整数，其数值范围是 [−231, 231 − 1] 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/powx-n
 */
public class Code50_x的n次方 {


    /**
     * 解法二 递归
     * 直接利用递归吧：
     *
     * 对于 n 是偶数的情况，x^n=x^{n/2}*x^{n/2}
     * 对于 n 是奇数的情况，x^n=x^{n/2}*x^{n/2}*x
     * 时间复杂度：O（log（n）)
     * @param x
     * @param n
     * @return
     */
    public double powRecursion(double x, int n) {
        if (n == 0) {
            return 1;
        }
        //偶数的情况
        if ((n & 1) == 0) {
            double temp = powRecursion(x, n / 2);
            return temp * temp;
        } else { //奇数的情况
            double temp = powRecursion(x, n / 2);
            return temp * temp * x;
        }
    }


    /**
     * 当然对于这种递归的解法的话，还有一些其他的思路
     *
     * 对于 n 是偶数的情况：x^n=(x*x)^{n/2}
     * 对于 n 是奇数的情况：x^n=(x*x)^{n/2}*x
     *
     * 代码就很好写了。
     *
     * @param x
     * @param n
     * @return
     */
    public static double powRecursion2(double x, int n) {
        if (n == 0) {
            return 1;
        }
        //偶数的情况
        if ((n & 1) == 0) {
            return powRecursion2(x * x, n / 2);
        } else { //奇数的情况
            return powRecursion2(x * x, n / 2) * x;
        }
    }

    public static void main(String[] args) {
        System.err.println(powRecursion2(2d, 2));
        System.err.println(1/2);
        System.err.println(1%2);
        System.err.println(1&1);
        System.err.println(4&1);
    }


}
