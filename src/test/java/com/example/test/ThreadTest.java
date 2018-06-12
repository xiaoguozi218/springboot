package com.example.test;

/**
 * Created by MintQ on 2018/6/12.
 */
public class ThreadTest {

    public static void main(String[] args) {
        Thread t = new Thread(){
          public void run(){
              pong();
          }
        };
        t.run();
        System.out.println("ping");
    }

    static void pong() {
        System.out.println("pong");
    }
}
