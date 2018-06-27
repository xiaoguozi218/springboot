package com.example.concurrent;

/**
 * Created by MintQ on 2018/6/27.
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
 *
 */
public class ConcurrentLearn {

}
