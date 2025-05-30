package com.example.concurrent.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;

/**
 * Created by MintQ on 2018/6/15.
 *
 * 《进程、线程和协程》讲解：    进程是 资源分配 的最小单元，线程是 系统(cpu)调度 的最小单元！
 *
 *     进程：直白地讲，进程就是应用程序的启动实例。比如我们运行一个游戏，打开一个软件，就是开启了一个进程。进程拥有代码和打开的文件资源、数据资源、独立的内存空间。
 *     线程：线程从属于进程，是程序的实际执行者。一个进程至少包含一个主线程，也可以有更多的子线程。 线程拥有自己的栈空间。
 *     协程：协程，英文Coroutines，是一种比线程更加轻量级的存在。  正如一个进程可以拥有多个线程一样，一个线程也可以拥有多个协程。
 *          最重要的是，协程不是被操作系统内核所管理，而完全是由程序所控制（也就是在用户态执行）。
 *          这样带来的好处就是性能得到了很大的提升，不会像线程切换那样消耗资源。
 *
 *          协程的应用：Lua语言 ，Lua从5.0版本开始使用协程，通过扩展库coroutine来实现。
 *                    Python语言，正如刚才所写的代码示例，python可以通过 yield/send 的方式实现协程。在python 3.5以后， async/await 成为了更好的替代方案。
 *                    Go语言，Go语言对协程的实现非常强大而简洁，可以轻松创建成百上千个协程并发执行。
 *                    Java语言，如上文所说，Java语言并没有对协程的原生支持，但是某些开源框架模拟出了协程的功能，有兴趣的小伙伴可以看一看Kilim框架的源码：https://github.com/kilim/kilim
 *
 *     注意：1、首先要知道的是：进程和线程的 关注点 是不一样的：
 *             - 进程间资源是独立的，关注的是 通讯问题。
 *             - 线程间资源是共享的，关注的是 安全问题。
 *         2、进程、线程、协程的关系和区别：
 *              进程-拥有自己独立的堆和栈，既不共享堆，亦不共享栈，进程由操作系统调度。
 *              线程-拥有自己独立的栈和共享的堆，共享堆，不共享栈，线程亦由操作系统调度(标准线程是的)。
 *              协程-和线程一样共享堆，不共享栈，协程由程序员在协程的代码里 显示调度。- 执行协程只需要极少的栈内存（大概是4～5KB），默认情况下，线程栈的大小为1MB。
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
 *  《*》线程的生命周期： 在Java5以后，线程状态被明确定义在其公共内部枚举类型 java.lang.Thread.State中，分别是：
 *       1、新建（NEW）: 表示线程被创建出来还没真正启动的状态，可以认为它是个Java内部状态。
 *       2、就绪（RUNNABLE）：表示该线程已经在JVM中执行，当然由于执行需要计算资源，它可能是正在运行，也可能还在等待系统分配给它CPU时间片，在就绪队列里面排队。
 *       3、阻塞（BLOCKED）： 这个状态和我们前面两讲介绍的同步非常相关，阻塞表示线程在等待Monitor lock。比如，线程试图通过synchronized去获取某个锁，但是其他线程已经独占了，那么当前线程就会处于阻塞状态。
 *       4、等待（WAITING）、计时等待（TIMED_WAITING）：表示正在等待其他线程采取某些操作。一个常见的场景是类似生产者消费者模式，发现任务条件尚未满足，就让当前消费者线程等待（wait）,另外的生产者线程去准备任务数据，
 *                                                  然后通过类似notify等动作，通知消费线程可以继续工作了。Thread.join()也会令线程进入等待状态。
 *                                                  计时等待进入条件和等待状态类似，但是调用的是存在超时条件的方法。
 *       5、终止（TERMINATED）：不管是意外退出还是正常执行结束，线程已经完成使命，终止运行，也有人把这个状态叫做死亡。
 *
 *     注意: 在其他一些分析中，会额外区分一种状态RUNNING,但是从Java API的角度，并不能表示出来。
 *
 *
 *  一、Java内存模型与线程
 *
 *
 *
 *
 *  二、线程安全性
 *     ~什么是线程安全： 线程安全就是说 多线程访问同一段代码，不会产生 不确定 的结果（二义性）。
 *                    也就是说 多线程每次运行结果 和 单线程运行的结果 是一样的，而且其他变量的值也和 预期的 是一样的，就是线程安全的。
 *
 *     ~线程安全实现方法：（深入理解JVM 中总结）
 *         1、互斥同步(阻塞同步)：
 *                      ~同步块 Synchronized：~入口：monitorenter
 *                                           ~出口：monitorexit
 *                      ~重入锁 ReentrantLock：1、等待可中断  2、公平锁   3、锁绑定多个条件
 *                      注意：悲观并发策略，线程的 阻塞 和 唤醒 都需要消耗性能
 *         2、非阻塞同步：~基于冲突检测的 乐观 并发策略 - CAS
 *                          ~先进行操作，如果没有线程竞争共享资源，则成功
 *                          ~如果产生竞争，采取其他措施（不断尝试，直至成功）
 *                      ~需硬件支持 - 操作和冲突检测需要原子性
 *                          ~具备原子操作的指令：CAS、Test And Set、Swap
 *         3、无同步方案：~线程本地存储ThreadLocal: ThreadLocal主要解决 多线程 中数据因并发产生不一致问题。
 *                          ~synchronized是利用锁的机制，使变量或代码块在某一时刻只能被一个线程访问。而ThreadLocal为每一个线程都提供了变量的副本，使得每个线程在某一时间访问到的并不是同一个对象，
 *                           这样就隔离了多个线程对数据的数据共享。而Synchronized却正好相反，它用于在多个线程间通信时能够获得数据共享。
 *                      ~线程安全(final) - 不可变、绝对线程安全
 *
 *
 *
 *  三、安全发布对象
 *
 *  发布对象：使一个对象能够被当前范围之外代码所使用。
    对象逸出：一种错误的发布。当一个对象还没有构造完成，就能被其它线程所见。
 *
 *  安全发布对象：
     在静态初始化函数中初始化一个对象的引用；
     将对象的引用保存到volatile类型域或者AtomicReference对象中；
     对象引用保存到某个正确构造对象final类型域中；
     将对象的引用保存到一个由锁保护的域中。

     对此，单例模式是个很好的学习例子：

 *  四、线程安全策略
 *
 *      1、不可变对象
 *      2、线程封闭
 *      3、线程不安全写法
 *      4、同步容器
 *      5、线程安全-并发容器-J.U.C
 *
 *  五、J.U.C之 AQS
 *      【1】使用Node实现FIFO队列，可以用于构建锁或者其它同步装置的基础框架；
 *      【2】利用了一个int类型表示状态；
 *      【3】使用方法是继承；
 *      【4】子类通过继承并通过实现它的方法管理锁的状态，对应AQS中acquire和release的方法操纵锁状态；
 *      【5】可以同步实现排它锁和共享锁模式（独占、共享）。
 *
 *    AQS同步组件：CountDownLatch、Semaphore、CyclicBarrier、ReentrantLock、ReentrantReadWriteLock、Condition、FutureTask、Fork/Join、BlockingQueue
 *      1、等待多线程完成的CountDownLatch（JDK1.5）
 *          允许一个或多个线程等待其他线程完成操作。    其构造函数接收一个int类型的参数作为计数器，调用CountDown方法的时候，计数器的值会减1，CountDownLatch的await方法会阻塞当前线程，直到N变为零。
 *          应用：并行计算，解析Excel中多个Sheet的数据。
 *      2、控制并发线程数的Semaphore
 *          用来控制同时访问特定资源线程的数量。  应用：流量控制，特别是公共资源有限的场景，如数据库连接。
 *          //可用的许可的数量  Semaphore(int permits)
 *          //获取一个许可    aquire()
 *          //使用完成后归还许可 release()
 *          //尝试获取许可证   tryAcquire()
 *      3、同步屏障CyclicBarrier
 *          让一组线程达到一个屏障（同步点）时被阻塞，直到最后一个线程到达屏障时，才会开门，所有被屏障拦截的线程才会继续执行。
 *          应用：多线程计算数据，最后合并计算结果的场景。
 *          CyclicBarrier和CountDownLatch的区别：    CountDownLatch计数器只能使用一次，CyclicBarrier可以调用reset()方法重置。所以CyclicBarrier可以支持更加复杂的场景，如发生错误后重置计数器，并让线程重新执行。
 *          //屏障拦截的线程数量     CyclicBarrier(int permits)
 *          //已经到达屏障    await()
 *          //CyclicBarrier阻塞线程的数量  getNumberWaiting()
 *      4、重入锁ReentrantLock （排他锁：同时允许单个线程访问。）
 *          支持重进入的锁，表示该锁能够支持一个线程对资源的重复加锁，即实现重进入：任意线程获取到锁之后能够再次获取该锁而不会被锁阻塞。
 *          该锁支持获取锁时的公平和非公平性选择：public ReentrantLock(boolean fair) {sync = fair ? new FairSync() : new NonfairSync();}
 *              公平锁就是等待时间最长的线程最优先获取锁，也就是说获取锁的是顺序的（FIFO），而非公平则允许插队。
 *              非公平因为不保障顺序，则效率相对较高，而公平锁则可以减少饥饿发生的概率：
 *                  [1]、提供了一个Condition类，可以分组唤醒需要唤醒的线程；
 *                  [2]、提供能够中断等待锁的线程机制，lock.lockInterruptibly()
 *      5、ReentrantReadWriteLock （读写锁，实现悲观读取，同时允许多个线程访问）
 *          在写线程访问时，所有读线程和其他写线程均被堵塞。其维护了一对锁，通过分离读锁、写锁，使得并发性比排他锁有很大提升。   适用于读多写少的环境，能够提供比排他锁更好的并发与吞吐量。
 *          不足：ReentrantReadWriteLock是读写锁，在多线程环境下，大多数情况是读的情况远远大于写的操作，因此可能导致写的饥饿问题。
 *          StampedLock ： 是ReentrantReadWriteLock 的增强版，是为了解决ReentrantReadWriteLock的一些不足。
 *              StampedLock读锁并不会阻塞写锁，设计思路也比较简单，就是在读的时候发现有写操作，再去读多一次。StampedLock有两种锁，一种是悲观锁，另外一种是乐观锁，如果线程拿到乐观锁就读和写不互斥，如果拿到悲观锁就读和写互斥。
 *
 *      6、Condition
 *          Condition提供了类似Object的监视器方法，依赖Lock实现等待/通知模式。
 *              【1】await():当前线程进入等待状态直到被通知或中断，当前线程进入运行状态且从await()方法返回；
 *              【2】signal():唤醒一个在Condition上的线程，该线程从等待方法返回前必须获得与Condition相关联的锁。
 *      7、FutureTask
 *          用于异步获取执行结果或取消执行任务的场景。（实现基于AQS）
 *      8、Fork/Join
 *          并行执行任务，即把大任务分割成若干小任务并行执行，最后汇总成大任务结果的框架。
 *              工作窃取算法：指的是某个线程从其他队列里窃取任务来执行。即这个队列先干完活，再去帮别人干点。
 *      9、BlockingQueue
 *          阻塞队列是一个支持两个附加操作的队列：
 *              [1]阻塞插入：当队列满的时候，队列会阻塞插入元素的线程，直到队列不满。
 *              [2]阻塞移除：当队列为空时，获取元素的线程就会等待队列变为非空。
 *          通常用于生产者和消费者场景。生产者是向队列里添加元素的线程，消费者是从列里获取元素的线程。阻塞队列就是生产者放元素，消费者获取元素的容器。(FIFO)
 *              ArrayBlockingQueue
 *              LinkedBlockingQueue
 *              PriorityBlockingQueue
 *              DelayQueue
 *              SynchronousQueue
 *              LinkedTransferQueue
 *              LinkedBlockingDeque
 *
 *
 *  六、线程与线程池
 *      线程：程序独立执行的最小单位（基本单位），可以理解成进程中独立运行的子任务。
        进程：一旦程序运行起来就变成了操作系统当中的一个进程。
 *  线程的创建:  1、继承Thread类       ：局限性：Java单继承，不易于扩展。
 *             2、实现Runnabl接口    ：
 *             3、实现Callable      ：运行Callable任务可以拿到一个Future对象，进行异步计算。
 *             4、线程池  : newCachedThreadPool     创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。潜在问题线程如果创建过多可能内存溢出。
 *                         newFixedThreadPool      创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
 *                         newScheduledThreadPool   创建一个定长线程池，支持定时及周期性任务执行。
 *                         newSingleThreadExecutor  创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序（FIFO、LIFO、优先级）执行。
 *
 *  为了保证共享数据的完整性，Java中引入了互斥锁的概念，即每个对象对应一个“互斥锁”的标记（monitor），用来保证任何时刻只能有一个线程访问该对象。利用Java中每个对象都拥有唯一的一个监视锁（monitor），当线程拥有这个标记时才会允许访问这个资源，而未拿到标记则进入阻塞，进入锁池。每个对象都有自己的一个锁池的空间，用于存放等待线程。由系统决定哪个线程拿到锁标记并运行。
 *
 *
        方法：currentThread():当前调用线程的相关信息；
             isAlive():判断当前线程是否处于活动状态；
             getId():线程的唯一标识；
             interrupted():测试当前线程是否已经中断；注：线程终端状态由该方法清除，意味着连续两次执行此方法，第二次将返回false。
             isInterrupted():测试线程是否已经中断； 注：不清楚状态标志。
             run(): 线程执行的具体方法，执行完成的会进入消亡状态；
             start():使县城出局就绪状态，等待调用线程的对象执行run()方法；
             sleep():让当前线程放弃CPU时间片直接返回就绪状态。
             yield():让当前线程放弃CPU时间片直接返回就绪状态。但放弃的时间片不确定，可能刚刚放弃，便立即获取。
 *
 *      线程通信： join(): 让当前线程邀请调用方法的线程优先执行，在被邀请的线程执行结束之前，邀请别人的线程不再执行，处于阻塞状态，直到被邀请的线程执行结束之后，进入就绪状态；
                 interrupt(): 中断、打断线程的阻塞状态。直接让阻塞状态的线程返回就绪，由sleep()、join()导致的阻塞立刻解除；
                 wait():使当前执行代码的线程放弃monitor并进入等待状态，直到接收到通知或被中断为止（notify）。即此时线程将释放自己的所有锁标记和CPU占用，同时进入这个对象的等待池（阻塞状态）。只能在同步代码块中调用（synchronized）；
                 notify():在等待池中随机唤醒一个线程，放入锁池，对象处于等待状态，直到获取对象的锁标记为止。 只能在同步代码块中调用（synchronized）。

 *
 * 七、Java并发之 Executor 框架：
 *  - Executor框架简介：Java的线程既是工作单元，也是执行机制。从JDK5开始，把工作单元和执行机制分离开来。
 *      Executor框架由3大部分组成任务：1、被执行任务需要实现的接口：Runnable接口或Callable接口。
 *                                 2、异步计算的结果。Future接口和FutureTask类。
 *                                 3、任务的执行。两个关键类ThreadPoolExecutor和ScheduledThreadPoolExecutor ？。
 *  - 概念介绍：
 *    - Runnable与Callable的区别：(1)Callable规定的方法是call(),Runnable规定的方法是run().
 *                              (2)Callable的任务执行后可返回值，而Runnable的任务是不能返回值的
 *                              (3)call方法可以抛出异常，run方法不可以
 *                              (4)运行Callable任务可以拿到一个Future对象，Future 表示异步计算的结果。
 *    - Future接口:Future接口代表 异步计算的结果，通过Future接口提供的方法可以查看异步计算是否执行完成，或者等待执行结果并获取执行结果，同时还可以取消执行。
 *
 *    - 任务的执行机制:
 *      - ThreadPoolExecutor: ThreadPoolExecutor是Executor框架最核心的类，是线程池的实现类。
 *          核心配置参数包括: - corePoolSize：核心线程池的大小
 *                         - maximumPoolSize：最大线程池的大小
 *                         - BlockingQueue：暂时保存任务的工作队列
 *                         - RejectedExecutionHandler：当ThreadPoolExecutor已经饱和时（达到了最大线程池大小且工作队列已满）将执行的Handler。
 *
 *
 *
 *
 *  面试题：
 *  1、一个线程调用两次start()方法会出现什么情况？
 *          答：Java的线程是 不允许 启动两次的，第二次调用必然会抛出 IllegalThreadStateException，这是一种运行时异常，多次调用start被认为是编程错误。
 *  2、在Java中CycliBarriar和CountdownLatch有什么区别？   这个线程问题主要用来检测你是否熟悉JDK5中的并发包。
 *      这两个的区别是CyclicBarrier可以重复使用已经通过的障碍，
 *      而CountdownLatch不能重复使用。
 *  3、什么是ThreadLocal类，怎么使用它？
 *      ThreadLocal是一个线程级别的 局部变量，并非“本地线程”。ThreadLocal为每个使用该变量的线程提供了一个独立的变量副本，每个线程修改副本时不影响其它线程对象的副本(译者注)。
 *      下面是线程局部变量(ThreadLocal variables)的关键点：
 *          一个线程局部变量(ThreadLocal variables)为每个线程方便地提供了一个 单独的变量。
 *          ThreadLocal实例通常作为静态的私有的(private static)字段出现在一个类中，这个类用来关联一个线程。
 *          当多个线程访问ThreadLocal实例时，每个线程维护ThreadLocal提供的独立的变量副本。
 *          常用的使用可在DAO模式中见到，当DAO类作为一个单例类时，数据库链接(connection)被每一个线程独立的维护，互不影响。(基于线程的单例)
 *  4、如何减少线程上下文切换？- 使用多线程时，不是多线程能提升程序的执行速度，使用多线程是为了更好地利用CPU资源！
 *     - 程序在执行时，多线程是CPU通过给每个线程分配CPU时间片来实现的，时间片是CPU分配给每个线程执行的时间，因时间片非常短，所以CPU通过不停地切换线程执行。
 *     - 线程不是越多就越好的，因为线程上下文切换是有性能损耗的，在使用多线程的同时需要考虑如何减少上下文切换
 *     - 一般来说有以下几条经验：
 *          1、CAS算法。Java的Atomic包使用CAS算法来更新数据，而不需要加锁。
 *          2、控制线程数量。避免创建不需要的线程，比如任务很少，但是创建了很多线程来处理，这样会造成大量线程都处于等待状态
 *          3、协程。在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换。
 *             - 协程可以看成是用户态自管理的“线程”。不会参与CPU时间调度，没有均衡分配到时间。非抢占式的
 *          4、无锁并发编程。多线程竞争时，会引起上下文切换，所以多线程处理数据时，可以用一些办法来避免使用锁，如将数据的ID按照Hash取模分段，不同的线程处理不同段的数据
 *
 */
public class ThreadLearn {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 获取java线程的管理MXBean
//        ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
//        // 不需要获取同步的Monitor和synchronizer信息，仅获取线程和线程堆栈信息
//        ThreadInfo[] threadInfos = tmxb.dumpAllThreads(false, false);
//        // 遍历线程信息，打印出ID和名称
//        for (ThreadInfo info : threadInfos) {
//            System.out.println("[" + info.getThreadId() + "] " + info.getThreadName());
//        }
        /**
         * [1] main
         * [2] Reference Handler
         * [3] Finalizer    处理用户的Finalizer方法
         * [4] Signal Dispatcher    外部jvm命令的转发器
         * [5] Attach Listener
         * [6] Monitor Ctrl-Break
         *
         */
        //一个使用Callable的简单例子
        callableTest();


    }

    public static void callableTest() throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        Callable call = new Callable() {
            @Override
            public Object call() throws Exception {
                ThreadLocal threadLocal = new ThreadLocal();
                threadLocal.set("threadLocal");
                Thread.sleep(5000); //休眠指定的时间，此处表示该操作比较耗时
                return  "Other less important but longtime things.";
            }
        };

        Future task = executorService.submit(call);

        //重要的事情

        System.out.println("Let's do important things. start");

        Thread.sleep(1000 * 3);

        System.out.println("Let's do important things. end");

        //不重要的事情

        while(!task.isDone()){

            System.out.println("still waiting....");

            Thread.sleep(1000 * 1);

        }

        System.out.println("get sth....");

        String obj = (String) task.get();

        System.out.println(obj);

        //关闭线程池

        executorService.shutdown();

    }


}
