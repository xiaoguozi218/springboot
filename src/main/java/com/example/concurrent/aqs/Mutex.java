package com.example.concurrent.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 互斥锁
 * Created by Administrator on 2018/11/17.
 * 要想使用AQS为我们服务，首先得弄懂三个问题：AQS是什么？AQS已经做了什么?以及我们还需要做些什么？
 * 1、AQS简介
 *  AQS是J.U.C包下AbstractQueuedSynchronizer抽象的队列式的同步器的简称，这是一个抽象类，它定义了一套多线程访问共享资源的同步器框架，
 *  J.U.C包下的许多同步类实现都依赖于它，比如ReentrantLock/Semaphore/CountDownLatch，可以说这个抽象类是J.U.C并发包的基础。
 *
 * 2、用AQS写一把互斥锁
 *  互斥锁是为了 保证数据的安全，在任一时刻只能有一个线程访问该对象。
 *  由上一个小节我们可知，AQS已经为我们实现所有 排队和阻塞 机制，我们只需要调用getState()、setState(int) 和
 *  compareAndSetState(int, int) 方发来维护state变量的数值和调用setExclusiveOwnerThread/getExclusiveOwnerThread来维护当前占用的线程是谁就行了。
 *
 */
public class Mutex implements Lock{
    //AQS子类的对象，Mutex互斥锁用它来工作
    private Sync sync = new Sync();

    //Sync同步器类作为公共内部帮助器，可用它来实现其封闭类的同步属性
    private class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            assert arg == 1; //这里用到了断言，互斥锁，锁只能被获取一次，如果arg不等于1,则直接中断
            if(this.compareAndSetState(0, 1)) { //这里做一下判断，如果state的值为等于0,立马将state设置为1
                //返回true,告诉acqure方法，获取锁成功
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //释放锁,由于这是一把互斥锁，state不是0就是1,所以你需要做两步：
            //1.直接将state置为0
            this.setState(0);

            //返回true,告诉aqs的release方法释放锁成功
            return true;
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
        // TODO Auto-generated method stub
    }

    @Override
    public boolean tryLock() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Condition newCondition() {
        // TODO Auto-generated method stub
        return null;
    }
}
