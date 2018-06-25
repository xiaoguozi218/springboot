package com.example.concurrent.lock;

/**
 * Created by MintQ on 2018/6/25.
 *
 * 避免线程死锁方式一
 *  加锁顺序，让线程按照一定的顺序加锁
 */
public class UnDeadLock_01 {
    public int flag = 1;
    //共享的静态对象类
    private static Object o1 = new Object(), o2 = new Object();
    public void money(int flag) {
        this.flag = flag;
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println("当前运行的线程是："+Thread.currentThread().getName()+" flag的值是："+flag);
                }
            }

        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("当前运行的线程是："+Thread.currentThread().getName()+" flag的值是："+flag);
                }
            }
        }
    }

    public static void main(String[] args) {
        final UnDeadLock_01 td1 = new UnDeadLock_01();
        final UnDeadLock_01 td2 = new UnDeadLock_01();
        td1.flag = 1;
        td2.flag = 0;
        //td1,td2都处于可执行状态，但JVM线程调度先执行哪一个并不确定
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                td1.flag = 1;
                td1.money(1);
            }
        });
        t1.start();
        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //让t2等待t1执行完
                try {
                    t1.join();  //核心代码，让t1执行完后再执行t2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                td2.flag = 0;
                td1.money(0);
            }
        });
        t2.start();
    }
}
