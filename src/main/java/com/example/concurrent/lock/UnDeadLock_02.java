package com.example.concurrent.lock;

import jdk.nashorn.internal.ir.ReturnNode;
import sun.util.resources.cldr.ka.LocaleNames_ka;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by MintQ on 2018/6/25.
 *
 * 防止线程死锁方式二
 *  加锁时限，超时则放弃请求该锁，并释放自己占有的锁
 *
 */
public class UnDeadLock_02 {
    public int flag = 1;
    //共享的静态对象类
    public Object o1 = new Object(), o2 = new Object();
    public void money(int flag) throws InterruptedException {
        this.flag = flag;
        if (flag == 1) {
            synchronized (o1) {
                Thread.sleep(500);
                synchronized (o2) {
                    System.out.println("当前运行的线程是："+Thread.currentThread().getName()+" flag的值是："+flag);
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                Thread.sleep(500);
                synchronized (o1) {
                    System.out.println("当前运行的线程是："+Thread.currentThread().getName()+" flag的值是："+flag);
                }
            }
        }
    }

    public static void main(String[] args) {
        final Lock lock = new ReentrantLock();
        final UnDeadLock_02 td1 = new UnDeadLock_02();
        final UnDeadLock_02 td2 = new UnDeadLock_02();
        td1.flag = 1;
        td2.flag = 0;
        //JVM调度线程并不知道td1,td2哪个先执行
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name  = Thread.currentThread().getName();
                td1.flag = 1;
                try {
                    //核心语句，获取不到锁，就等待5秒，如果5秒还是获取不到锁则返回false
                    if (lock.tryLock(5000, TimeUnit.MILLISECONDS)) {
                        System.out.println(name + "获取到锁！");
                    } else {
                        System.out.println(name+"获取不到锁！！");
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    td1.money(1);
                } catch (InterruptedException e) {
                    System.out.println(name+" 出错了！！！");
                    e.printStackTrace();
                } finally {
                    System.out.println("当前运行的线程是："+name+" 释放锁-");
                    lock.unlock();
                }
            }
        });
        t1.start();

        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name  = Thread.currentThread().getName();
                td1.flag = 0;
                try {
                    //核心语句，获取不到锁，就等待5秒，如果5秒还是获取不到锁则返回false
                    if (lock.tryLock(5000, TimeUnit.MILLISECONDS)) {
                        System.out.println(name + "获取到锁！");
                    } else {
                        System.out.println(name+"获取不到锁！！");
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    td2.money(0);
                } catch (InterruptedException e) {
                    System.out.println(name+" 出错了！！！");
                    e.printStackTrace();
                } finally {
                    System.out.println("当前运行的线程是："+name+" 释放锁-");
                    lock.unlock();
                }
            }
        });
        t2.start();
    }
}
