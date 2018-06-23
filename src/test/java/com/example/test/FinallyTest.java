package com.example.test;

/**
 * Created by MintQ on 2018/5/17.
 */
public class FinallyTest {


    public static void main(String[] args) {

        //列几个 finally 不会被执行的情况：
        //1、try-catch  异常退出。

//        try {
//            System.exit(1);
//        } finally {
//            System.out.println("finally out ");
//        }

        //2、无限循环

//        try {
//            while (true) {
//            System.out.println("try out ...");
//            }
//        } finally {
//            System.out.println("finally out ...");
//        }
        // 3、线程被杀死  当执行try,finally的线程被杀死时，finally 也无法执行。

        System.err.println(a());
        System.err.println(target);

    }

    static int target = 8;

    public static int a() {
        try {

            return target;
        } catch (Exception e) {
            return 9;
        } finally {
            target = 10;
            return  target;
        }


    }
}
