package com.example.concurrent.mq;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by MintQ on 2018/5/9.
 */
public class ProducerConsumer {

    public static void main(String[] args) {
        BlockingDeque<Product> queues = new LinkedBlockingDeque<Product>(10);

        ExecutorService service = Executors.newCachedThreadPool();

        Producer p = new Producer("张三",queues);
        Producer p2 = new Producer("李四",queues);
        Consumer c = new Consumer("王五",queues);
        Consumer c2 = new Consumer("老刘",queues);
        Consumer c3 = new Consumer("老李",queues);

        service.submit(p);
        service.submit(p2);
        service.submit(c);
        service.submit(c2);
        service.submit(c3);


    }

}
