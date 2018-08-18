package com.example.concurrent.lock.readwritelock;
/**
 * @author  xiaoguozi
 * @create  2018/8/18 下午3:35
 * @desc    Java读写锁实现原理 - 适用于 读多写少
 *
 * 一、为什么需要读写锁？
 *    - 与传统锁不同的是读写锁的规则是可以共享读，但只能一个写，
 *      总结起来为：读读不互斥，读写互斥，写写互斥，而一般的独占锁是：读读互斥，读写互斥，写写互斥，而场景中往往读远远大于写。
 *    - 注意是读远远大于写，一般情况下独占锁的效率低来源于高并发下对 临界区 的激烈竞争导致线程上下文切换。
 *      因此当并发不是很高的情况下，读写锁由于需要额外维护读锁的状态，可能还不如独占锁的效率高。因此需要根据实际情况选择使用。
 * 二、ReadWriteLock的实现原理
 *    - 在Java中ReadWriteLock的主要实现为 ReentrantReadWriteLock，其提供了以下特性：
 *         1、公平性选择：支持公平与非公平（默认）的锁获取方式，吞吐量非公平优先于公平。
 *         2、可重入：读线程获取读锁之后可以再次获取读锁，写线程获取写锁之后可以再次获取写锁
 *         3、可降级：写线程获取写锁之后，其还可以再次获取读锁，然后释放掉写锁，那么此时该线程是读锁状态，也就是降级操作。
 *    2.1、ReentrantReadWriteLock的结构
 *        - ReentrantReadWriteLock的核心是由一个基于AQS的同步器 Sync 构成，然后由其扩展出 ReadLock（共享锁），WriteLock（排它锁）所组成。
 *    2.2、Sync的实现
 *        - sync是读写锁实现的核心，sync是基于AQS实现的，在AQS中核心是 state字段 和 双端队列，那么一个一个问题来分析。
 *    2.3、Sync如何同时表示读锁与写锁？
 *        - 从代码中获取读写状态可以看出其是把state（int32位）字段分成高16位与低16位，其中高16位表示读锁个数，低16位表示写锁个数
 *    2.4、读锁的获取
 *        - 读锁的获取主要实现是AQS中的acquireShared方法
 *    2.5、读锁的释放
 *        - 读锁的释放主要是tryReleaseShared(arg)函数，因此拆解其步骤如下：
 *          1、操作1：清理ThreadLocal中保存的获取锁数量信息
 *          2、操作2：CAS修改读锁个数，实际上是自减一
 *    2.6、写锁的获取 - 写锁的获取也主要是tryAcquire(arg)方法，这里也拆解步骤：
 *    2.7、写锁的释放 - 写锁的释放主要是tryRelease(arg)方法，其逻辑就比较简单了
 *
 **/
public class ReadWriteLockLearn {


    /**
     * 读锁持有个数
     */
    private int readCount = 0;
    /**
     * 写锁持有个数
     */
    private int writeCount = 0;

    /**
     * 获取读锁,读锁在写锁不存在的时候才能获取
     */
    public synchronized void lockRead() throws InterruptedException {
        // 写锁存在,需要wait
        while (writeCount > 0) {
            wait();
        }
        readCount++;
    }

    /**
     * 释放读锁
     */
    public synchronized void unlockRead() {
        readCount--;
        notifyAll();
    }

    /**
     * 获取写锁,当读锁存在时需要wait.
     */
    public synchronized void lockWrite() throws InterruptedException {
        // 先判断是否有写请求
        while (writeCount > 0) {
            wait();
        }

        // 此时已经不存在获取写锁的线程了,因此占坑,防止写锁饥饿
        writeCount++;

        // 读锁为0时获取写锁
        while (readCount > 0) {
            wait();
        }
    }

    /**
     * 释放读锁
     */
    public synchronized void unlockWrite() {
        writeCount--;
        notifyAll();
    }

}
