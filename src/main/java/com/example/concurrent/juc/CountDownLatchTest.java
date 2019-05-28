package com.example.concurrent.juc;

import java.util.concurrent.CountDownLatch;

/***
 * CountDownLatch，允许一个或多个线程等待某些操作完成。
 *
 * 实际开发中经常用于监听某些初始化操作，等待初始化完成后，通知主线程继续工作，它相当于一个栅栏
 * 下面，来看看 CountDownLatch 和 CyclicBarrier，它们的行为有一定的相似度，经常会被考察二者有什么区别，我来简单总结一下。
 *  1） CountDownLatch 是不可以重置的，所以无法重用；而 CyclicBarrier 则没有这种限制，可以重用。
 *  2）CountDownLatch 的基本操作组合是 countDown/await。调用 await 的线程阻塞等待 countDown 足够的次数，不管你是在一个线程还是多个线程里 countDown，
 *      只要次数足够即可。所以就像 Brain Goetz 说过的，CountDownLatch 操作的是事件。
 *  3）CyclicBarrier 的基本操作组合，则就是 await，当所有的伙伴（parties）都调用了 await，才会继续进行任务，并自动进行重置。注意，正常情况下，
 *      CyclicBarrier 的重置都是自动发生的，如果我们调用 reset 方法，但还有线程在等待，就会导致等待线程被打扰，抛出 BrokenBarrierException 异常。
 *      CyclicBarrier 侧重点是线程，而不是调用事件，它的典型应用场景是用来等待并发线程结束。
 *
 *
 *
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
