package com.example.concurrent;

import java.util.concurrent.Semaphore;

/**
 * Created by MintQ on 2018/6/29.
 *
 * 限流：利用 Semaphore 信号量 进行控制。
     限流在保障系统稳定和避免过载，起着举足轻重的作用。
     限流在形式上主要有三种形式：
         1、无等待队列
         2、有等待队列
         3、异步处理请求：当用户请求到来时，立即返回。而请求的数据压入消息队列。后端通过不断的轮训消息队列，进行业务处理
 *
 */
public class SemaphoreLimit {

    // 信号量的定义
    public static Integer MAX_VISITOR_VOLUME = 100;
    public static Semaphore semaphore = new Semaphore(MAX_VISITOR_VOLUME);

    public static Integer MAX_WAIT_QUEUE_LENGTH = 40;



    //无等待队列-只是限制总的访问量，一旦达到总访问量阈值，直接拒绝访问. 此处的访问量阈值取决于具体的网络环境。
    public void request() {
        // 流量控制
        // 存在等待队列，意味着请求量已经超过 MAX_VISITOR_VOLUME，则直接返回
        if (semaphore.getQueueLength() > 0) {
            return;
        }
        try {
            // 获取一个信号量，获取不到则等待
            semaphore.acquire();
            // TODO 业务处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放一个信号量
            semaphore.release();
        }
    }

    //有等待队列-允许一定量的请求等待，适用于IO密集型的应用，等待队列的长度取决于网络IO和硬盘IO
    public void request2() {
        // 流量控制
        // 等待队列的长度超过 MAX_WAIT_QUEUE_LENGTH，则直接返回
        if (semaphore.getQueueLength() > MAX_WAIT_QUEUE_LENGTH) {
            return;
        }
        try {
            // 获取一个信号量，获取不到则等待
            semaphore.acquire();
            // TODO 业务处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放一个信号量
            semaphore.release();
        }
    }



}
