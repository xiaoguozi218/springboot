package com.example.test;

/**
 * Created by MintQ on 2018/5/17.
 */
public class StringTest {




    public static void main(String[] args) {

        String s1 = "123";

        String s2 = "123";

        String news3 = new String("123");

        System.out.println(s1==s2);

        System.out.println(s1==news3);

        System.out.println(s1);

        System.out.println(s2);

        System.out.println(news3.intern());




    }
}
