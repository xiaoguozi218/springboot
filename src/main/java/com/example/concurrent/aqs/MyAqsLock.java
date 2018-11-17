package com.example.concurrent.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 用AQS实现的 重入锁
 * Created by Administrator on 2018/11/17.
 *
 * 上面的Mutex并非一把可重入锁，为了实现这把锁能够让同一线程多次进来，回忆一下上一篇博客中怎么实现的？
 * 当时的做法是在锁的lock()自旋方法中判断新进来的是不是正在运行的线程，如果新进来的线程就是正在运行的线程，则获取锁成功，并让计数器+1。
 * 而在释放锁的时候，如果释放锁的线程等于当前线程，让计数器-1，只有当计数器count归零的时候才真正的释放锁。同样的，用AQS实现的锁也是这个思路
 */
public class MyAqsLock implements Lock{
    //AQS子类的对象，用它来辅助MyAqsLock工作
    private Sync sync = new Sync();

    private class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            //如果第一个线程进来，直接获得锁，并设置当前独占的线程为当前线程
            int state = this.getState();
            if(state == 0) { //state为0,说明当前没有线程占用该线程
                if(this.compareAndSetState(0, arg)) { //判断当前state值，第一个线程进来，立刻设置state为arg
                    this.setExclusiveOwnerThread(Thread.currentThread()); //设置当前独占线程为当前线程
                    return true; //告诉顶级aqs获取锁成功
                }
            } else { //如果是第二个线程进来
                Thread currentThread = Thread.currentThread();//当前进来的线程
                Thread ownerThread = this.getExclusiveOwnerThread();//已经保存进去的独占式线程
                if(currentThread == ownerThread) { //判断一下进来的线程和保存进去的线程是同一线程么？如果是，则获取锁成功，如果不是则获取锁失败
                    this.setState(state+arg); //设置state状态
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //锁的获取和锁的释放是一一对应的,获取过多少次锁就释放多少次锁
            if(Thread.currentThread() != this.getExclusiveOwnerThread()) {
                //如果释放锁的不是当前线程，则抛出异常
                throw new RuntimeException();
            }
            int state = this.getState()-arg;
            //接下来判断state是否已经归零，只有state归零的时候才真正的释放锁
            if(state == 0) {
                //state已经归零，做扫尾工作
                this.setState(0);
                this.setExclusiveOwnerThread(null);
                return true;
            }
            this.setState(state);
            return false;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    /**
     * 上锁的方法
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 释放锁的方法
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        //调用帮助器的tryAcquire方法，测试获取锁一次，不会自旋
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        //调用帮助器的tryRelease方法，测试释放锁一次，不会子旋
        return sync.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        //调用帮助类获取Condition对象
        return sync.newCondition();
    }
}
