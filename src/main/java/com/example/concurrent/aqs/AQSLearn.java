package com.example.concurrent.aqs;
/**
 * Java并发编程：用AQS写一把可重入锁
 * @author  gsh
 * @date  2018/11/17 上午11:42
 *
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
 *
 **/
public class AQSLearn {
}
