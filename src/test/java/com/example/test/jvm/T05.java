package com.example.test.jvm;

/**
 * Created by MintQ on 2018/6/23.
 * 线程栈大小:   -Xss128k
 */
public class T05 {

    static int count = 0;

    static void r() {
        count++;
        r();
    }

    public static void main(String[] args) {
        try {
            r();
        } catch (Throwable e) {
            System.out.println(count);
            e.printStackTrace();    //OOM stackOverFlow
        }
    }

}
