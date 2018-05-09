package com.example.concurrent.mq;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by MintQ on 2018/5/9.
 * 生产者
 *
 * 生产者消费者模式是一个经典的多线程设计模式，它为多线程间的协作提供了良好的解决方案。在生产者消费者模式中，通常有两类线程，即若干个生产者线程和若干个消费者线程。生产者线程足额提交用户请求，消费者线程负责具体处理生产者提交的任务。生产者和消费者之间则通过共享内存缓冲区进行通信。
 *
 */
public class Producer implements Runnable {

    private String name;
    BlockingQueue<Product> s;

    public Producer(String name, BlockingQueue<Product> s) {
        this.name = name;
        this.s = s;
    }

    @Override
    public void run() {


            try {
                while (true) {
                    Product product = new Product((int) (Math.random() * 10000)); //产生0~9999随机整数
                    System.out.println(name + "准备生产(" + product.toString() + ").");
                    s.put(product);
                    System.out.println(name + "已生产(" +product.toString()+").");
                    System.out.println("==========");
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
