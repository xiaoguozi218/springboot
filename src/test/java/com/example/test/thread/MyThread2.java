package com.example.test.thread;

public class MyThread2 extends Thread{

    public void run() {
        for (int i = 0; i <=1000 ; i++) {
            System.out.println("Thread2第" +i + "个");
        }
    }
}
