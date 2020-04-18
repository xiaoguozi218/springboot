package com.example.algorithm.btyedance;
/**
 * 题目二：
 * 给定长度为m的字符串aim，以及一个长度为n的字符串str，问能否在str中找到一个长度为m的连续子串，使得这个子串刚好由aim
 * 的m个字符组成，顺序无所谓，返回任意满足条件的一个子串的起始位置，未找到返回-1
 *
 * @author  gsh
 * @date  2020/4/18 上午9:38
 **/
public class TongYuanYiGouTi {

    /**
     * 最优解
     * @author  gsh
     * @date  2020/4/18 上午9:53
     * @Param
     **/
    public static int test1(String s,String a){
        char[] aim = a.toCharArray();
        int[] count = new int[256];
        for (int i = 0; i < aim.length; i++) {
            count[aim[i]]++;
        }

        int M = aim.length;
        char[] str = s.toCharArray();
        int inValidTimes = 0;
        int R = 0; //右边界
        //先让窗口拥有M个字符
        for (; R < M ; R++) {
            if (count[str[R]]-- <=0){
                inValidTimes++;
            }
        }
        for (; R < str.length; R++) {
            if(inValidTimes == 0){
                return R - M;
            }
            if(count[str[R]]-- <= 0){
                inValidTimes++;
            }
            if(count[str[R-M]]++ < 0){
                inValidTimes--;
            }
        }
        return inValidTimes == 0 ? R-M: -1;
    }
}
