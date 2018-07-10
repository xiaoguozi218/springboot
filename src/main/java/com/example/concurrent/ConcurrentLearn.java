package com.example.concurrent;

/**
 * Created by MintQ on 2018/6/27.
 *
 *  我们进行多线程编程，无非是达到几个目的：
 *      1、利用多线程提高程序的扩展能力，以达到业务对吞吐量的要求。
 *      2、协调线程间调度、交互，以完成业务逻辑。
 *      3、线程间传递数据和状态，这同样是实现业务逻辑的需要。
 *
 * 一、前言
 * Java 并发编程实践中的话：
    编写正确的程序并不容易，而编写正常的并发程序就更难了。相比于顺序执行的情况，多线程的线程安全问题是微妙而且出乎意料的，因为在没有进行适当同步的情况下多线程中各个操作的顺序是不可预期的。
 *
 *  主要讲解内容如下：
 *      1、rt.jar 中 Unsafe 类主要函数讲解， Unsafe 类提供了硬件级别的原子操作，可以安全的直接操作内存变量，其在 JUC 源码中被广泛的使用，了解其原理为研究 JUC 源码奠定了基础。
 *      2、rt.jar 中 LockSupport 类主要函数讲解，LockSupport 是个工具类，主要作用是挂起和唤醒线程，是创建锁和其它同步类的基础，了解其原理为研究 JUC 中锁的实现奠定基础。
 *      3、讲解 JDK8 新增原子操作类 LongAdder 实现原理，并讲解 AtomicLong 的缺点是什么，LongAdder 是如何解决 AtomicLong 的缺点的，LongAdder 和 LongAccumulator 是什么关系？
 *      4、JUC 并发包中并发组件 CopyOnWriteArrayList 的实现原理，CopyOnWriteArrayList 是如何通过写时拷贝实现并发安全的 List？
 * 二、 Unsafe 类探究
 *  JDK 的 rt.jar 包中的 Unsafe 类提供了 硬件级别 的 原子操作，Unsafe 里面的方法都是 native 方法，通过使用 JNI 的方式来访问本地 C++ 实现库。
 *  下面我们看下 Unsafe 提供的几个主要方法以及编程时候如何使用 Unsafe 类做一些事情。
 *
 *  2.1 主要方法介绍
 *      long objectFieldOffset(Field field) 方法。作用：返回指定的变量在所属类的内存偏移地址，偏移地址仅仅在该 Unsafe 函数中访问指定字段时候使用。如下代码使用 unsafe 获取AtomicLong 中变量 value 在 AtomicLong 对象中的内存偏移。
 *      int arrayBaseOffset(Class arrayClass) 方法-获取数组中第一个元素的地址
 *      int arrayIndexScale(Class arrayClass) 方法-获取数组中单个元素占用的字节数
 *      boolean  compareAndSwapLong(Object obj, long offset, long expect, long update) 方法-比较对象 obj 中偏移量为 offset 的变量的值是不是和 expect 相等，相等则使用 update 值更新，然后返回 true，否者返回 false
 *      public native long getLongVolatile(Object obj, long offset) 方法-获取对象 obj 中偏移量为 offset 的变量对应的 volatile 内存语义的值。
 *      void putLongVolatile(Object obj, long offset, long value) 方法-设置 obj 对象中内存偏移为 offset 的 long 型变量的值为 value，支持 volatile 内存语义。
 *      void putOrderedLong(Object obj, long offset, long value) 方法-设置 obj 对象中 offset 偏移地址对应的 long 型 field 的值为 value。这是有延迟的 putLongVolatile 方法，并不保证值修改对其它线程立刻可见。变量只有使用 volatile 修饰并且期望被意外修改的时候使用才有用。
 *  下面是 Jdk8 新增的方法，这里简单的列出 Long 类型操作的方法
 *      long getAndSetLong(Object obj, long offset, long update) 方法-获取对象 obj 中偏移量为 offset 的变量 volatile 语义的值，并设置变量 volatile 语义的值为 update。
 *
 *  2.2 如何使用 Unsafe 类
 *      我们知道 Unsafe 类是在 rt.jar 里面提供的，而 rt.jar 里面的类是使用 Bootstrap 类加载器加载的，而我们启动 main 函数所在的类是使用 AppClassLoader 加载的。
        所以在 main 函数里面加载 Unsafe 类时候鉴于委托机制会委托给 Bootstrap 去加载 Unsafe 类。
        如果没有代码（2.2.8）这鉴权，那么我们应用程序就可以随意使用 Unsafe 做事情了，而 Unsafe 类可以直接操作内存，是不安全的。
        所以 JDK 开发组特意做了这个限制，不让开发人员在正规渠道下使用 Unsafe 类，而是在 rt.jar 里面的核心类里面使用 Unsafe 功能。
        那么如果开发人员真的想要实例化 Unsafe 类，使用 Unsafe 的功能该如何做那？
        方法有很多种，既然正规渠道访问不了，那么就玩点黑科技，使用万能的 反射 来获取 Unsafe 实例方法：
 *
 *  三、LockSupport类探究
 *      JDK 中的 rt.jar 里面的 LockSupport 是个工具类，主要作用是挂起和唤醒线程，它是创建锁和其它同步类的基础。
 *
 *
 * 《*》Java并发包提供了哪些并发工具类？
 *      答：我们通常所说的并发包也就是java.util.concurrent及其子包，集中了Java并发的各种基础工具类，具体主要包括几个方面：
 *          1、提供了比synchronized更加高级的各种同步结构，包括CountDownLatch、CyclicBarrier、Semaphore等，可以实现更加丰富的多线程操作，比如利用Semaphore作为资源控制器，限制同时进行工作的线程数量。
 *          2、各种线程安全的容器，比如最常见的ConcurrentHashMap、有序的ConcurrentSkipListMap，或者通过类似快照机制，实现线程安全的动态数组CopyOnWriteArrayList等。
 *          3、各种并发队列实现，如各种BlockedQueue实现，比较典型的ArrayBlockingQueue、SynchrousQueue或针对特定场景的PriorityBlockingQueue等。
 *          4、强大的Executor框架，可以创建各种不同类型的线程池，调度任务运行等，绝大部分情况下，不再需要自己从头实现线程池和任务调度器。
 *
 *
 * 《*》什么是高并发 ，详细讲解
 *     一、什么是高并发
 *      ~高并发（High Concurrency）是互联网 分布式系统架构 设计中必须考虑的因素之一，它通常是指，通过设计保证系统能够同时并行处理很多请求。
 *      ~高并发相关常用的一些指标有 响应时间（Response Time），吞吐量（Throughput），每秒查询率QPS（Query Per Second），并发用户数 等。
 *          响应时间：系统对请求做出响应的时间。例如系统处理一个HTTP请求需要200ms，这个200ms就是系统的响应时间。
 *          吞吐量：  单位时间内处理的请求数量。
 *          QPS：    每秒响应请求数。在互联网领域，这个指标和吞吐量区分的没有这么明显。
 *          并发用户数：同时承载正常使用系统功能的用户数量。例如一个即时通讯系统，同时在线量一定程度上代表了系统的并发用户数。
 *     二、如何提升系统的并发能力
 *      ~互联网分布式架构设计，提高系统并发能力的方式，方法论上主要有两种：垂直扩展（Scale Up）与水平扩展（Scale Out）。
 *          1、垂直扩展：提升单机处理能力。垂直扩展的方式又有两种：（1）增强单机硬件性能，例如：增加CPU核数如32核，升级更好的网卡如万兆，升级更好的硬盘如SSD，扩充硬盘容量如2T，扩充系统内存如128G；
 *                                                         （2）提升单机架构性能，例如：使用Cache来减少IO次数，使用异步来增加单服务吞吐量，使用无锁数据结构来减少响应时间；
 *              不管是提升单机硬件性能，还是提升单机架构性能，都有一个致命的不足：单机性能总是有极限的。所以互联网分布式架构设计高并发 终极解决方案 还是 水平扩展。
 *          2、水平扩展：只要增加服务器数量，就能线性扩充系统性能。水平扩展对系统架构设计是有要求的，如何在架构各层进行可水平扩展的设计，以及互联网公司架构各层常见的水平扩展实践，是本文重点讨论的内容。
 *
 *     三、常见的互联网分层架构
 *
 * 《*》经典并发原语:
 *      ~锁（Lock，也叫作互斥锁，Mutex）：保证只有一个线程能进入指定的代码区域；
 *      ~监视器：功能和锁一样，但比锁好一些，因为使用锁时必须要解锁；
 *      ~（计数的）信号量（Semaphore）：一种强大的抽象概念，能支持多种协同场景；
 *      ~等待并通知：功能相同，但比信号量弱一些，因为程序员必须在等待之前处理丢失的通知触发；
 *      ~条件变量：当某个条件触发时让线程睡眠或唤醒；
 *      ~带有条件等待的通道和缓冲区：如果没有线程接收信息的话，则监听并收集信息（可以选择有边界的缓冲区）；
 *      ~非阻塞数据结构（如非阻塞队列、原子计数器等）：这些智能数据结构支持从多个数据结构中访问，而无需使用锁，或者将锁的使用控制在最少
 *    这些原语的功能有重叠。任何编程语言只需要几个原语就能得到并发的全部力量。例如，锁和信号量就能完成你能想到的任何并发场景。
 *
 *
 *
 *
 *
 */
public class ConcurrentLearn {

//    String str = "{\"t\":{\"appId\":\"bhhj\",\"applicationStatus\":\"CREATED\",\"businessId\":\"hbw\",\"channel\":\"BHHJ_MF_baidu\",\"createdTime\":1531118705496,\"customerId\":30301726,\"device\":\"ANDROID\",\"deviceId\":\"581345932\",\"id\":280791,\"orderNo\":\"IM2018070914450549687745\",\"quickApplyFlag\":false},\"timestamp\":\"1531118705504\",\"topic\":\"im_core_application\"}";

}
