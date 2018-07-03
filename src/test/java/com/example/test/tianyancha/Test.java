package com.example.test.tianyancha;

import org.omg.PortableServer.THREAD_POLICY_ID;

/**
 * Created by MintQ on 2018/7/3.
 */
public class Test {

    public static void main(String[] args) {

//        String str1 = "hello";
//        String str2 = "he" + new String("llo");
//        System.out.println(str1 == str2);   //false

//        Thread t = new Thread(){
//            public void run(){
//                pong();
//            }
//        };
//
//        t.run();
//        System.out.print("ping");   //pongping

        System.out.println(getValue(2));    //10
    }

    static void pong() {
        System.out.print("pong");
    }


    public static int getValue(int i) {
        int result = 0;
        switch (i) {
            case 1:
                result = result+i;
            case 2:
                result = result+ i*2;
            case 3:
                result = result+ i*3;
        }
        return result;
    }
}
