package com.example.test;

/**
 * Created by MintQ on 2018/5/14.
 */
public class JVMTest {

    public static int method() {
        return 1;
    }

    public static void main(String[] args) {
        String m = method() + "";
        String m1 = method() + "";
        System.out.println(m == m1);

        String m2 = 1 + "";
        String m3 = 1 + "";
        System.out.println(m2 == m3);

        int a = 1;
        String m4 = a + "";
        String m5 = a + "";
        System.out.println(m4 == m5);

        final int b = 1;
        String m6 = b + "";
        String m7 = b + "";
        System.out.println(m6 == m7);
    }


}
