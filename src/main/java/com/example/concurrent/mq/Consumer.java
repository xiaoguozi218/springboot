package com.example.concurrent.mq;

import javax.annotation.security.RunAs;
import java.util.concurrent.BlockingDeque;

/**
 * Created by MintQ on 2018/5/9.
 *
 * 消费者
 */
public class Consumer implements Runnable {

    private String name;
    BlockingDeque<Product> s;


    public Consumer(String name, BlockingDeque<Product> s) {
        this.name = name;
        this.s = s;
    }

    @Override
    public void run() {

        try {
            while (true) {
                System.out.println(name + "准备消费产品。");
                Product product = s.take();
                System.out.println(name + "已消费("+product.toString()+").");
                System.out.println("==========");
                Thread.sleep(500);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
