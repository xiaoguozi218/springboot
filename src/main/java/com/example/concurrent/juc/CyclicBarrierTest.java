package com.example.concurrent.juc;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author  gsh
 * @date  2018/12/13 上午11:10
 * 倒数计时器。假设有一个场景：每个线程代表一个跑步运动员，当运动员都准备好后，才能一起出发，只要有一个人没有准备好，大家都等待：
 *
 **/
public class CyclicBarrierTest {

    static class Runner implements Runnable {
        private CyclicBarrier barrier;
        private String name;

        public Runner(CyclicBarrier barrier, String name) {
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000*(new Random().nextInt(5)));
                System.out.println(name + " 准备OK。");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(name + "GO!!");
        }
    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(new Thread(new Runner(barrier,"张三")));
        executorService.submit(new Thread(new Runner(barrier,"李四")));
        executorService.submit(new Thread(new Runner(barrier,"王五")));

        executorService.shutdown();
    }
}
