package com.example.algorithm.alibaba;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 阿里面试题
 *    Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 * 1. Open brackets must be closed by the same type of brackets.
 * 2. Open brackets must be closed in the correct order.
 * Note that an empty string is also considered valid.
 */
public class CharMatch {

    static Map<String,String> map = new HashMap<String,String>();
    static{
        map.put("(",")");
        map.put("[","]");
        map.put("{","}");
    }

    private static String opend=  "({[";
    private static String close = ")}]";

    /**
     * () true
     * (] false
     * ()[]{}  true
     * {()}  true
     *
     *
     * @param str
     * @return
     */

    public static  boolean cheched(String str){
        if (StringUtils.isBlank(str) || str.length() == 1) {
            return false;
        } else if(str.length() % 2==0) {
            String[] split = str.split("");
            ArrayList<String> arrayList = new ArrayList<>();//从左向右遍历 记录开
            for(String str_ : split){
                //如果是开
                if(opend.contains(str_)){
                    arrayList.add(str_);
                }if(close.contains(str_)){
                    int lastIndex = arrayList.size()-1;
                    String lastOpen = arrayList.get(lastIndex);
                    String getClose = map.get(lastOpen);
                    if(str_.equals(getClose)){// 说明匹配，开始消除
                        arrayList.remove(lastIndex);
                    }else{
                        return false;
                    }

                }
            }
            //说明被消完，完全匹配
            if(arrayList.size()==0){
                return true;
            }else{
                return false;
            }

        }

        return false;
    }

    /**
     * 方法二
     * @author  gsh
     * @date  2020/4/26 下午1:15
     * @Param s
     * @return
     **/
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(')');
            }
            else if (c == '{') {
                stack.push('}');
            }
            else if (c == '[') {
                stack.push(']');
            }
            else if (stack.isEmpty() || stack.pop() != c) {
                return false;
            }
        }
        return stack.isEmpty();
    }


//    @Test
//        public void test1(){
//            boolean cheched = CharMatch.cheched("{()[]{}}");
//            System.out.println("checked : "+cheched);
//    }

    public static void main(String[] args) {
//        boolean cheched = CharMatch.cheched("{()[]{}}");
        boolean cheched = isValid("{()}");
        System.out.println("checked : "+cheched);
    }

}
