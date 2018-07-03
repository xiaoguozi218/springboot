package com.example.test;

import javax.swing.table.TableCellRenderer;

/**
 * Created by MintQ on 2018/6/12.
 */
public class ThreadTest implements Runnable{

    public static void main(String[] args) throws InterruptedException {
//        Thread t = new Thread(){
//          public void run(){
//              pong();
//          }
//        };
//        t.run();
//        System.out.println("ping");
        ThreadTest tt = new ThreadTest();
        new Thread(tt).start();
        tt.m2();
        System.out.println("main thread b= "+tt.b);
    }

    static void pong() {
        System.out.println("pong");
    }

    int b = 100;
    synchronized void m1() throws InterruptedException {
        b = 1000;
        Thread.sleep(500);
        System.out.println("b="+b);
    }

    synchronized void m2() throws InterruptedException {
//        Thread.sleep(250);
        b=2000;
    }

    @Override
    public void run() {
        try {
            m1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
