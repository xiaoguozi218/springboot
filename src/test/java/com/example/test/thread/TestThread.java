package com.example.test.thread;

public class TestThread {
    public static void main(String[] args) {
        Thread t1 = new MyThread1();
        Thread t2 = new MyThread2();
        t2.start();
        t1.start();
    }
}
