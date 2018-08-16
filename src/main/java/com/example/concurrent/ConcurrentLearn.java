package com.example.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

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
 *
 * 《*》C10K 问题总结：
 *     1、C10K问题由来：- 随着互联网的普及，应用的用户群体几何倍增长，此时服务器性能问题就出现。最初的服务器是基于进程/线程模型。新到来一个TCP连接，就需要分配一个线程。假如有C10K，就需要创建1W个线程，可想而知单机是无法承受的。
 *                      那么如何突破单机性能是高性能网络编程必须要面对的问题，进而这些局限和问题就统称为C10K问题，最早是由Dan Kegel进行归纳和总结的，并且他也系统的分析和提出解决方案。
 *     2、C10K问题的本质： - C10K问题的本质上是操作系统的问题。对于Web 1.0/2.0时代的操作系统，传统的同步阻塞I/O模型处理方式都是 一请求一线程。
 *                         当创建的进程或线程多了，数据拷贝频繁（缓存I/O、内核将数据拷贝到用户进程空间、阻塞，进程/线程上下文切换消耗大， 导致操作系统崩溃，这就是C10K问题的本质。
 *                       - 可见, 解决C10K问题的关键就是尽可能减少这些CPU资源消耗。
 *     3、C10K问题的解决方案 - 从网络编程技术的角度来说，主要思路：- 1、每个连接分配一个独立的线程/进程
 *                                                            2、同一个线程/进程同时处理多个连接
 *          - 每个进程/线程处理一个连接: - 该思路最为直接，但是申请进程/线程是需要系统资源的，且系统需要管理这些进程/线程，所以会使资源占用过多，可扩展性差
 *          - 每个进程/线程同时处理 多个连接(I/O多路复用): 1、select 2、poll 3、epoll
 *              1、select方式：使用 fd_set结构体 告诉内核同时监控哪些文件句柄，使用 逐个排查方式(轮询) 去检查是否有文件句柄就绪或者超时。
 *                            该方式有以下缺点：- 文件句柄数量是有上线的，- 逐个检查吞吐量低，- 每次调用都要重复初始化fd_set。
 *              2、poll方式：该方式主要解决了select方式的2个缺点，文件句柄上限问题(链表方式存储)以及重复初始化问题(不同字段标注关注事件和发生事件)，
 *                          但是逐个去检查文件句柄是否就绪的问题(轮询问题)仍然没有解决。
 *              3、epoll方式：该方式可以说是C10K问题的killer，他不去轮询监听所有文件句柄是否已经就绪。epoll只对发生变化的文件句柄感兴趣。
 *                           其工作机制是，使用"事件"的就绪通知方式，通过epoll_ctl注册文件描述符fd，一旦该fd就绪，内核就会采用类似callback的回调机制来激活该fd, epoll_wait便可以收到通知, 并通知应用程序。
 *                           而且epoll使用一个文件描述符管理多个描述符,将用户进程的文件描述符的事件存放到内核的一个事件表中, 这样数据只需要从内核缓存空间拷贝一次到用户进程地址空间。
 *                           而且epoll是通过内核与用户空间 共享内存方式 来实现事件就绪消息传递的，其效率非常高。但是epoll是依赖系统的(Linux)。
 *
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
 *《深入浅出乐观锁与悲观锁》：
 *  1、何谓悲观锁与乐观锁 - 乐观锁总是乐观的认为请求不会存在竞争，悲观锁总是悲观的认为每次请求都会存在竞争。这两种人各有优缺点，不能不以场景而定说一种人好于另外一种人。
 *
 *  2、悲观锁 -
 *      - 总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞直到它拿到锁
 *        （共享资源每次只给一个线程使用，其它线程阻塞，用完后再把资源转让给其它线程）。
 *      - 传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁。Java中synchronized和ReentrantLock等独占锁就是悲观锁思想的实现。
 *  3、乐观锁 -
 *      - 总是假设最好的情况，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用1、版本号机制和 2、CAS算法实现。
 *      - 乐观锁适用于 多读 的应用类型，这样可以 提高吞吐量，像数据库提供的类似于write_condition机制，其实都是提供的乐观锁。
 *        在Java中java.util.concurrent.atomic包下面的 原子变量类 就是使用了乐观锁的一种实现方式CAS实现的。
 *  4、两种锁的使用场景
 *      - 乐观锁：适用于写比较少的情况下（多读场景）- 即冲突很少发生的时候，这样可以省去了锁的开销，加大了系统的整个吞吐量。
 *      - 悲观锁：一般 多写的场景 下用悲观锁就比较合适。
 *  5、乐观锁常见的两种实现方式 - 1、版本号机制 - 一般是在数据表中加上一个数据版本号version字段
 *                          - 2、CAS算法   - 即compare and swap（比较与交换），是一种有名的无锁算法。
 *      5.2、CAS算法涉及到三个操作数：- 需要读写的内存值 V
 *                                - 进行比较的值 A
 *                                - 要写入的新值 B
 *          - 当且仅当 V 的值等于 A时，CAS通过原子方式用新值B来更新V的值，否则不会执行任何操作（比较和替换是一个原子操作）。一般情况下是一个自旋操作，即不断的重试。
 *      5.3、乐观锁的缺点：
 *          - 1、ABA 问题是乐观锁一个常见的问题
 *          - 2、循环时间长开销大 - 自旋CAS（也就是不成功就一直循环执行直到成功）如果长时间不成功，会给CPU带来非常大的执行开销。
 *          - 3、只能保证一个共享变量的原子操作 - CAS 只对单个共享变量有效，当操作涉及跨多个共享变量时 CAS 无效。
 *
 *《java高并发总结-锁-常用于面试复习》
 *  1、定义 -
 *      独占锁：它是一种悲观保守的加锁策略，它避免了读/读冲突，如果某个只读线程获取锁，则其他读线程都只能等待，这种情况下就限制了不必要的并发性，因为读操作并不会影响数据的一致性。
 *      共享锁：共享锁则是一种乐观锁，它放宽了加锁策略，允许多个执行读操作的线程同时访问共享资源。
 *
 *  2、分类 - 独占锁： ReentrantLock， ReentrantReadWriteLock.WriteLock
 *         - 共享锁：ReentrantReadWriteLock.ReadLock，CyclicBarrier， CountDownLatch和Semaphore都是共享锁
 *  3、其他 - wait(), notify() ，对应Contition的 await 和 signal
 *         - 前者是object，在代码中用锁对象即可调用，后者是一个即可，必须使用ReentrantLock生成
 *  4、注意事项
 *      - 除了Synchronized自动走出锁范围，其余占有了锁，一定要记得释放锁，可以在finally中释放!!!!
 *      - 公平参数，默认都是关闭的，所以不要以为等的时间越长就能更大几率获得锁！公平和非公平对应着：公平锁和非公平锁（Sync类的两个子类），非公平锁无视等待队列，直接上来就是抢！
 *      - java实现锁的底层都是使用Sync类，private final Sync sync; - 是继承了AbstractQueuedSynchronizer（AQS）的内部抽象类，主要由它负责实现锁的功能。
 *          - 关于 AbstractQueuedSynchronizer 只要知道它内部存在一个获取锁的等待队列及其互斥锁状态下的int状态位（0当前没有线程持有该锁、n存在某线程重入锁n次）即可，该状态位也可用于其它诸如共享锁、信号量等功能。
 *
 *
 */
public class ConcurrentLearn {

//    String str = "{\"t\":{\"appId\":\"bhhj\",\"applicationStatus\":\"CREATED\",\"businessId\":\"hbw\",\"channel\":\"BHHJ_MF_baidu\",\"createdTime\":1531118705496,\"customerId\":30301726,\"device\":\"ANDROID\",\"deviceId\":\"581345932\",\"id\":280791,\"orderNo\":\"IM2018070914450549687745\",\"quickApplyFlag\":false},\"timestamp\":\"1531118705504\",\"topic\":\"im_core_application\"}";
    public static void main(String[] args) {
        //Demo code：
        //1、 CountDownLatch - 用于n个线程等待其余M个线程结束, 类位于java.util.concurrent包下
//        CountDownLatch doneSignal = new CountDownLatch(3); // 定义了计数器为3.表示有3个线程结束即可！
//        for(int i=0; i<5; i++){
//            new InnerThread().start(); // m个线程，InnerThread的run方法末尾中写了doneSignal.countDown(); 必须手动减
//        }
//        doneSignal.await(); // 阻塞，直到3个线程结束
        //  await(long timeout, TimeUnit unit) throws InterruptedException { }; 这个就可以实现超时功能

        //2: CyclicBarrier - java.util.concurrent包，实现 M 个线程在barrier栅栏处互相等待，可以重用状态（所以叫cycli），1的计数器只能减不能重新赋值！

        //3： Semaphore - java.util.concurrent包，用于限制最多M个线程同时并发

        //4： ReentrantLock - java.util.concurrent包，用于实现同步功能，与synchronized相似但是面向对象更灵活
        ReentrantLock takeLock = new ReentrantLock();
        // 获取锁
        takeLock.lock();
        try {
        // 业务逻辑
        } finally {
        // 释放锁
            takeLock.unlock();
        }

        //5：ReentrantReadWriteLock -

        //8：Condition接口 （需要与ReentrantLock配合使用）- 条件变量很大一个程度上是为了解决Object.wait/notify/notifyAll难以使用的问题，也就是消费者生产者代码中判断队列是否满，是否空的条件！
    }
}
