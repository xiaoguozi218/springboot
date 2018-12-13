package com.example.concurrent.juc;

import java.util.concurrent.CountDownLatch;

/***
 * 实际开发中经常用于监听某些初始化操作，等待初始化完成后，通知主线程继续工作，它相当于一个栅栏
 *
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        //new CountDownLatch(2)表示await 过后，得有两个countDown才能唤醒
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("进入线程t1"+"等待其他线程处理完成。。。");
                    countDownLatch.await();
                    System.out.println("t1线程继续执行。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("t2线程进行初始化操作。。。");
                    Thread.sleep(3000);
                    System.out.println("t2线程初始化完毕，通知t1线程继续。。。");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2");

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("t3线程进行初始化操作。。。");
                    Thread.sleep(4000);
                    System.out.println("t3线程初始化完毕，通知t1线程继续。。。");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t3");

        t1.start();
        t2.start();
        t3.start();

    }
}
