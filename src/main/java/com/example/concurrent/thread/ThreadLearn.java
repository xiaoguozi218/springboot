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
 *  一、Java内存模型与线程
 *
 *
 *
 *
 *  二、线程安全性
 *
 *
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
 *
 *
 *
 */
public class ThreadLearn {



}
