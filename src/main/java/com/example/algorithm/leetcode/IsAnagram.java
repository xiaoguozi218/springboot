package com.example.algorithm.leetcode;
/**
 * leetcode 242. 有效的字母异位词
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 *
 * 示例 1:
 *
 * 输入: s = "anagram", t = "nagaram"
 * 输出: true
 * 示例 2:
 *
 * 输入: s = "rat", t = "car"
 * 输出: false
 * 说明:
 * 你可以假设字符串只包含小写字母。
 *
 * 进阶:
 * 如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？
 * @author  gsh
 * @date  2020/5/1 下午10:02
 * @Param 
 * @return 
 * @throws 
 **/
public class IsAnagram {

    /**
     * 该题只需要判断两个字符串是否所有的字符都是相同的即可，具体做法如下：首先初始化一个长度为26的数组（因为英文字母有26个），
     * 遍历字符串s并用数组的值来记录各个字母出现的个数；再遍历字符串t并将指定的字母位置减一（相当于抵消的操作）。
     * 最后遍历数组arr，如果全部为0则代表所有字母都是相同的：
     * @author  gsh
     * @date  2020/5/1 下午10:04
     **/
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] arr = new int[26];

        for (int i = 0; i < s.length(); i++) {
            arr[s.charAt(i) - 'a']++;
            arr[t.charAt(i) - 'a']--;
        }

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        IsAnagram isAnagram = new IsAnagram();
        boolean anagram = isAnagram.isAnagram("cat", "tac");
        System.err.println(anagram);
        System.err.println('c' - 'a');
    }

}
