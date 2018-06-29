package com.example.concurrent.lock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by MintQ on 2018/6/29.
 */
public class DeadLockSample extends Thread{

    private String first;
    private String second;

    public DeadLockSample(String name,String first,String second) {
        super(name);
        this.first=first;
        this.second=second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName()+" obtained:"+first);
            try {
                Thread.sleep(1000);
                synchronized (second) {
                    System.out.println(this.getName()+" obtained:"+second);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        Runnable dlChenk = new Runnable() {
            @Override
            public void run() {
                long[] threadIds = mbean.findDeadlockedThreads();
                if (threadIds != null) {
                    ThreadInfo[] threadInfos = mbean.getThreadInfo(threadIds);
                    System.out.println("Detected deadlock threads:");
                    for (ThreadInfo threadInfo:threadInfos) {
                        System.out.println(threadInfo.getThreadName());
                    }
                }
            }
        };

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        //稍等5秒，然后每10秒进行一次死锁扫描
        scheduledExecutorService.scheduleAtFixedRate(dlChenk,5L,10L, TimeUnit.SECONDS);

        String lockA = "lockA";
        String lockB = "lockB";
        DeadLockSample t1 = new DeadLockSample("Thread1",lockA,lockB);
        DeadLockSample t2 = new DeadLockSample("Thread2",lockB,lockA);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

    }
}
