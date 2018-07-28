package com.example.concurrent.thread.threadpool;

/**
 * @author  xiaoguozi
 * @create  2018/7/28 下午10:36
 * @desc    深入学习 Java 线程池 - https://mp.weixin.qq.com/s/Iw2k-QkRlj1HwBZWCL9w2A
 *
 *《深入学习 Java 线程池》：- 线程池是多线程编程中的核心概念，简单来说就是一组可以执行任务的空闲线程。
 *  一、为什么使用线程池？
 *      - 创建并开启一个线程开销很大。如果我们每次需要执行任务时重复这个步骤，那将会是一笔巨大的性能开销，这也是我们希望通过多线程解决的问题。
 *      - 线程池通过减少需要的线程数量并管理线程生命周期，来帮助我们缓解性能问题。
 *      - 本质上，线程在我们使用前一直保存在线程池中，在执行完任务之后，线程会返回线程池等待下次使用。这种机制在执行很多小任务的系统中十分有用。
 *
 *  二、Java 线程池 -
 *      - Java 通过 executor 对象来实现自己的线程池模型。可以使用 executor 接口或其他线程池的实现，它们都允许细粒度的控制。
 *      - java.util.concurrent 包中有以下接口：
 *          - Executor —— 执行任务的简单接口
 *          - ExecutorService —— 一个较复杂的接口，包含额外方法来管理任务和 executor 本身
 *          - ScheduledExecutorService —— 扩展自 ExecutorService，增加了执行任务的调度方法
 *
 *      1、Executors 类和 Executor 接口
 *          Executors 类包含工厂方法创建不同类型的线程池，Executor 是个简单的线程池接口，只有一个 execute() 方法。
 *          Executors 类里的工厂方法可以创建很多类型的线程池：
 *              - newSingleThreadExecutor()：包含单个线程和无界队列的线程池，同一时间只能执行一个任务
 *              - newFixedThreadPool()：包含固定数量线程并共享无界队列的线程池；当所有线程处于工作状态，有新任务提交时，任务在队列中等待，直到一个线程变为可用状态
 *              - newCachedThreadPool()：只有需要时创建新线程的线程池
 *              - newWorkStealingThreadPool()：基于工作窃取（work-stealing）算法的线程池，后面章节详细说明
 *      2、ExecutorService
 *          - 创建 ExecutorService 方式之一便是通过 Excutors 类的工厂方法。- ExecutorService executor = Executors.newFixedThreadPool(10);
 *          - 除了 execute() 方法，接口也定义了相似的 submit() 方法，这个方法可以返回一个 Future 对象。
 *
 *  - 在下面的章节，我们来看一下 ExecutorService 接口的两个实现：ThreadPoolExecutor 和 ForkJoinPool。
 *      3、ThreadPoolExecutor
 *          - 这个线程池的实现增加了配置参数的能力。
 *      4、ForkJoinPool - 另一个线程池的实现是 ForkJoinPool 类。它实现了 ExecutorService 接口，并且是 Java 7 中 fork/join 框架的重要组件。
 *          - fork/join 框架基于“工作窃取算法”。简而言之，意思就是执行完任务的线程可以从其他运行中的线程“窃取”工作。
 *          - ForkJoinPool 适用于任务创建子任务的情况，或者外部客户端创建大量小任务到线程池。
 *          - 这种线程池的工作流程如下：
 *              1、创建 ForkJoinTask 子类
 *              2、根据某种条件将任务切分成子任务
 *              3、调用执行任务
 *              4、将任务结果合并
 *              5、实例化对象并添加到池中
 *          - 创建一个 ForkJoinTask，你可以选择 RecursiveAction 或 RecursiveTask 这两个子类，后者有返回值。
 *
 *  三、使用线程池的潜在风险
 *
 *  四、结论
 *      线程池有很大优势，简单来说就是可以将任务的执行从线程的创建和管理中分离。另外，如果使用得当，它们可以极大提高应用的性能。
 *      如果你学会充分利用线程池，Java 生态系统好处便是其中有很多成熟稳定的线程池实现。
 *
 **/
public class ThreadPoolLearn {

}
