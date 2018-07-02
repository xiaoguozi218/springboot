package com.example.concurrent.thread.communication.tongbu;

/**
 * Created by MintQ on 2018/7/2.
 */
public class Run {
    public static void main(String[] args) {
        /**
         * 1、同步：
         *    由于线程A和线程B持有同一个MyObject类的对象object，尽管这两个线程需要调用不同的方法，但是它们是同步执行的，
         *      比如：线程B需要等待线程A执行完了methodA()方法之后，它才能执行methodB()方法。这样，线程A和线程B就实现了 通信。
         *    这种方式，本质上就是“共享内存”式的通信。多个线程需要访问同一个共享变量，谁拿到了锁（获得了访问权限），谁就可以执行。
         *
         *
         *
         *
         */
        MyObject object = new MyObject();

        //线程A与线程B 持有的是同一个对象:object
        ThreadA a = new ThreadA(object);
        ThreadB b = new ThreadB(object);
        a.start();
        b.start();
    }

}
