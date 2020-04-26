package com.example.algorithm.alibaba;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程A、B、C，实现一个程序让线程A打印“阿”，线程B打印“里”，线程C打印“巴”，
 * 三个线程输出阿里巴巴阿里巴巴......阿里巴巴，循环10次“阿里巴巴”
 * @author  gsh
 * @date  2020/4/26 下午8:09
 **/
public class PrintAlibabaTest {
    private int times;
    private int state;
    private Lock lock = new ReentrantLock();

    public PrintAlibabaTest(int times) {
        this.times = times;
    }

    public void printA() {
        printNum("阿", 0);
    }

    public void printLi() {
        printNum("里", 1);
    }

    public void printBaBa() {
        printNum("巴巴", 2);
    }

    private void printNum(String name, int num) {
        for (int i = 0; i < times;) {
            lock.lock();
            if (state % 3 == num) {
                state++;
                i++;
                System.out.print(name);
            }
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        PrintAlibabaTest printAlibabaTest = new PrintAlibabaTest(10);
        new Thread(new Runnable() {
            @Override public void run() {
                printAlibabaTest.printA();
            } }).start();
        new Thread(new Runnable() {
            @Override public void run() {
                printAlibabaTest.printLi();
            } }).start();
        new Thread(new Runnable() {
            @Override public void run() {
                printAlibabaTest.printBaBa();
            } }).start();
    }

}
