package com.example.concurrent.thread;

/**
 * Created by MintQ on 2018/6/15.
 *
 *  Java的线程分为两种：User Thread(用户线程):
 *                    DaemonThread(守护线程): 只要当前JVM实例中尚存任何一个非守护线程没有结束，守护线程就全部工作；只有当最后一个非守护线程结束是，守护线程随着JVM一同结束工作，Daemon作用是为其他线程提供便利服务，守护线程最典型的应用就是GC(垃圾回收器)，他就是一个很称职的守护者。
 *     这里有几点需要注意：(1) thread.setDaemon(true)必须在thread.start()之前设置，否则会抛出一个IllegalThreadStateException异常。你不能把正在运行的常规线程设置为守护线程。
 *                      (2) 在Daemon线程中产生的新线程也是Daemon的。
 *                      (3) 不要认为所有的应用都可以分配给Daemon来进行服务，比如读写操作或者计算逻辑。写java多线程程序时，一般比较喜欢用java自带的多线程框架，比如ExecutorService，但是java的线程池会将守护线程转换为用户线程，所以如果要使用后台线程就不能用java的线程池。
 *
 *
 *
 *
 *  Java多线程的join和interrupt方法：
 *      Java的join()官方的解释: Waits for this thread to die.   等待当前线程直到当前线程结束
 *      假设现在有A、B，线程A在线程B内调用A.join()，那么线程B会被挂起，直到A线程完成后才恢复。
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class ThreadLearn {



}
