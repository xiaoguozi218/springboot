package com.example.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ThreadInfo;
import java.util.concurrent.Semaphore;

/**
 * Created by MintQ on 2018/6/29.
 *  Semaphore: 允许一个或多个线程等待某些操作完成。
 *
 *  总的来说，我们可以看出Semaphore就是个 计数器，其基本逻辑基于acquire/release，并没有太复杂哦的同步逻辑。
 *  如果Semaphore的数值被初始化为1，那么一个线程就可以通过acquire进入互斥状态，本质上和互斥锁是非常相似的。但是区别也非常明显，比如互斥锁是有持有者的，而对于Semaphore这种计数器结构，
 *      虽然有类似功能，但其实不存在真正意义的持有者，除非我们进行扩展包装。
 *
 */
public class SemaphoreLearn {
    private static final Logger logger = LoggerFactory.getLogger(SemaphoreLearn.class);

    public static void main(String[] args) {
        System.out.println("Action...Go!");
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new SemaphoreWorker(semaphore));
            t.start();
        }
    }

    static class SemaphoreWorker implements Runnable {
        private String name;
        private Semaphore semaphore;

        public SemaphoreWorker(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                log("is waiting for a permit!");
                semaphore.acquire();
                log("acquired a permit!");
                log("executed!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log("released a permit!");
                semaphore.release();
            }
        }
        private void log(String msg) {
            if (name == null) {
                name = Thread.currentThread().getName();
            }
            System.out.println(name+" "+msg);
        }

    }

}
