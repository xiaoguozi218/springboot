package com.example.concurrent.thread;

import java.util.Date;

/**
 * Created by MintQ on 2018/6/15.
 */
public class TestJoin {

    public static void main(String[] args) throws Exception {
        TestJoin testJoin = new TestJoin();
        Thread task = new Thread(testJoin.new Task());
        System.out.println("Begin Time: " + new Date());
        task.start();
//        task.join();
        System.out.println("Finish Time: " + new Date());
    }

    class Task implements Runnable {

        @Override
        public void run() {
            try {
//                Thread.currentThread().interrupt();
                Thread.sleep(2000);
                System.out.println("task: " + new Date());
            } catch (InterruptedException e) {
                System.out.println("Task is interrupted");
            }
        }
    }

}
