package com.example.concurrent.thread;

import java.util.Date;

/**
 * Created by MintQ on 2018/6/15.
 *
 * 如何保证多线程 顺序执行：
 *  有三个线程T1 T2 T3，如何保证他们按顺序执行-
    在T2的run中，调用t1.join，让t1执行完成后再让T2执行
    在T3的run中，调用t2.join，让t2执行完成后再让T3执行
 *
 *
 */
public class TestJoin {

    public static void main(String[] args) throws Exception {
//        TestJoin testJoin = new TestJoin();
//        Thread task = new Thread(testJoin.new Task());
//        System.out.println("Begin Time: " + new Date());
//        task.start();
////        task.join();
//        System.out.println("Finish Time: " + new Date());

        t2.start();
        t1.start();
        t3.start();
    }

//    class Task implements Runnable {
//
//        @Override
//        public void run() {
//            try {
////                Thread.currentThread().interrupt();
//                Thread.sleep(2000);
//                System.out.println("task: " + new Date());
//            } catch (InterruptedException e) {
//                System.out.println("Task is interrupted");
//            }
//        }
//    }

    static Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("t1");
        }
    });

    static Thread t2 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                t1.join();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("t2");
        }
    });

    static Thread t3 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                t2.join();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("t3");
        }
    });

}
