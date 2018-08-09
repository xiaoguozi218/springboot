package com.example.concurrent.jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by MintQ on 2018/6/28.
 *
 *  深入浅出JVM的锁优化案例
 *
 *  《*》锁优化 ：1、适应性自旋（Adaptive Spinning）： 线程阻塞的时候，让等待的线程不放弃cpu执行时间，而是执行一个自旋(一般是空循环)，这叫做自旋锁。
 *                  自旋等待本身虽然避免了线程切换的开销，但它是要占用处理器时间的，因此，如果 锁被占用的时间很短，自旋等待的效果就非常好，反之，如果锁被占用的时间很长，那么自旋的线程只会白白消耗处理器资源，带来性能上的浪费。
 *                  因此，自旋等待的时间必须要有 一定的限度。如果自旋超过了限定的次数仍然没有成功获得锁，就应当使用传统的方式去挂起线程了。自旋次数的默认值是 10 次，用户可以使用参数-XX：PreBlockSpin来更改。
 *                  JDK1.6引入了自适应的自旋锁。自适应意味着自旋的时间不再固定了，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。比如前一次自旋了3次就获得了一个锁，那么下一次虚拟机会允许他自旋更多次来获得这个锁。如果一个锁很少能通过自旋成功获得，那么之后再遇到这个情况就会省略自旋过程了。
 *              2、锁消除（Lock Elimination）    ：
 *                  虚拟机 即时编译器 在运行时，对一些代码上要求同步，但是被检测到不可能存在共享数据竞争的锁进行消除。一般根据 逃逸分析 的数据支持来作为判定依据。
 *              3、锁粗化（Lock Coarsening）     ：
 *                  原则上，我们在编写代码的时候，总是推荐将同步块的作用范围限制得尽量小——只在共享数据的实际作用域中才进行同步，这样是为了使需要同步的操作数量尽可能变小，如果存在锁竞争，那等待锁的线程也能尽快拿到锁。
 *                  但如果一系列操作频繁对同一个对象加锁解锁，或者加锁操作在 循环体内，会耗费性能，这时虚拟机会扩大加锁范围。
 *              4、轻量级锁（Lightweight Locking）：
 *                  轻量级锁是JDK 1.6之中加入的新型锁机制。它的作用是 在没有多线程竞争的前提下，减少传统的重量级锁 使用操作系统互斥量 产生的性能消耗。
 *                  HotSpot虚拟机的对象头（Object Header）分为两部分信息，第一部分用于存储 对象自身的运行时数据，这部分称为Mark Word。还有一部分存储 指向方法区 对象类型数据 的指针。
 *                  加锁：.... 如果有两条以上的线程争用同一个锁，那轻量级锁就不再有效，要膨胀为重量级锁，锁标志的状态值变为“10”，Mark Word中存储的就是指向重量级锁（互斥量）的指针，后面等待锁的线程也要进入阻塞状态。
 *                  解锁：解锁过程也是通过CAS操作来进行的。如果对象的Mark Word仍然指向着线程的锁记录，那就用CAS操作把对象当前的Mark Word和线程中复制的Displaced Mark Word替换回来，如果替换成功，整个同步过程就完成了。如果替换失败，说明有其他线程尝试过获取该锁，那就要在释放锁的同时，唤醒被挂起的线程。
 *                  性能：没有锁竞争时，轻量级锁用 CAS操作 替代 互斥量 的开销，性能较优。有锁竞争时，除了互斥量开销，还有CAS操作开销，所以性能较差。但是，一般情况下，在整个同步周期内都是不存在竞争的”，这是一个经验数据。
 *              5、偏向锁（Biased Locking）       ：
 *                  偏向锁也是JDK1.6中引入的锁优化，它的目的是 消除数据在无竞争情况下的同步原语，进一步提高程序的运行性能。
 *                  如果说 轻量级锁是在 无竞争的情况下 使用 CAS操作去消除同步使用的互斥量，那 偏向锁 就是在无竞争的情况下 把整个同步都消除掉，连CAS操作都不做了。
 *
 *                  当锁对象第一次被线程获取的时候，虚拟机将会把对象头中的标志位设为“01”，即偏向模式。同时使用CAS操作把获取到这个锁的线程的ID记录在对象的Mark Word之中，如果CAS操作成功，持有偏向锁的线程以后每次进入这个锁相关的同步块时，虚拟机都可以不再进行任何同步操作。当有另外一个线程去尝试获取这个锁时，偏向模式结束。
 *                  偏向锁可以提高带有同步但无竞争的程序性能，但并不一定总是对程序运行有利。如果程序中大多数的锁总是被多个不同的线程访问，那偏向模式就是多余的。在具体问题具体分析的前提下，有时候使用参数-XX：-UseBiasedLocking来禁止偏向锁优化反而可以提升性能。
 *
 *  《*》JVM内存结构、内存模型 、对象模型那些事
 *      一、JVM内存结构
 *          如上，做个总结，JVM内存结构，由Java虚拟机规范定义。描述的是Java程序执行过程中，由JVM管理的不同数据区域。各个区域有其特定的功能。
 *
 *      二、Java内存模型
 *          Java内存模型是根据英文Java Memory Model（JMM）翻译过来的。其实JMM并不像JVM内存结构一样是真实存在的。他只是一个 抽象 的概念。
 *          JSR-133: Java Memory Model and Thread Specification 中描述了，JMM是和多线程相关的，他描述了一组规则或规范，这个规范定义了一个线程对共享变量的写入时对另一个线程是可见的。
 *          那么，简单总结下，Java的多线程之间是通过共享内存进行通信的，而由于采用 共享内存 进行通信，在通信过程中会存在一系列如原子性、可见性、有序性等问题，而JMM就是围绕着多线程通信以及与其相关的一系列特性而建立的模型。
 *              JMM定义了一些语法集，这些语法集映射到Java语言中就是volatile、synchronized等关键字。
 *          在JMM中，我们把多个线程间通信的共享内存称之为主内存，而在并发编程中多个线程都维护了一个自己的本地内存（这是个抽象概念），其中保存的数据是主内存中的数据拷贝。而JMM主要是控制本地内存和主内存之间的数据交互的。
 *
 *      三、Java对象模型
 *          Java是一种面向对象的语言，而Java对象在JVM中的存储也是有一定的结构的。而这个关于Java对象自身的存储模型称之为Java对象模型。
 *          HotSpot虚拟机中，设计了一个OOP-Klass Model。OOP（Ordinary Object Pointer）指的是普通对象指针，而Klass用来描述对象实例的具体类型。
 *          每一个Java类，在被JVM加载的时候，JVM会给这个类创建一个instanceKlass，保存在方法区，用来在JVM层表示该Java类。当我们在Java代码中，使用new创建一个对象的时候，JVM会创建一个instanceOopDesc对象，这个对象中包含了对象头以及实例数据。
 *
 *      四、总结：
 *          我们再来区分下JVM内存结构、 Java内存模型 以及 Java对象模型 三个概念。
 *              ~JVM内存结构，和Java虚拟机的运行时区域有关。
 *              ~Java内存模型，和Java的并发编程有关。
 *              ~Java对象模型，和Java对象在虚拟机中的表现形式有关。
 *
 *
 * 《*》锁优化 之 偏向锁、轻量级锁、重量级锁 -
 *      背景 - 我听说人类写的代码中有些特殊的地方，叫做 临界区，比如synchronized修饰的 方法 或者 代码块，他们非常神奇，在同一时刻JVM老大只允许一个线程进入执行。
 *          - 每次设置锁我都得和操作系统打交道，请他在内核中维护一个什么Mutex（互斥量）的东西，他还得把你们这些线程阻塞，切换，这可是一笔巨大的费用啊，所以这些锁还是少用为妙。
 *          - 仔细一想，老大煞费心机地设置了 偏向锁和轻量级锁，就是为了避免阻塞，避免操作系统的介入。
 *      1、偏向锁 - 通常只有一个线程在临界区执行。
 *
 *      2、轻量级锁 - 可以有多个线程交替进入临界区，在 竞争不激烈 的时候，稍微自旋等待一下就能获得锁。
 *
 *      3、重量级锁 - 也是我最为期待的锁，那就是出现了激烈的竞争，只好让我们去 阻塞 休息了。
 *
 *
 * 《OSGI》：（面向Java的动态模块化系统）- OSGi(Open Service Gateway Initiative)技术是Java动态化模块化系统的一系列规范。
 *  1、如何正确的理解和认识OSGI技术？
 *      - 我们做软件开发一直在追求一个境界，就是模块之间的真正“解耦”、“分离”，这样我们在软件的管理和开发上面就会更加的灵活，甚至包括给客户部署项目的时候都可以做到更加的灵活可控。
 *        但是我们以前使用SSH框架等架构模式进行产品开发的时候我们是达不到这种要求的。
 *      - 所以我们“架构师”或顶尖的技术高手都在为 模块化开发 努力的摸索和尝试，然后我们的OSGI的技术规范就应运而生。
 *      - 现在我们的OSGI技术就可以满足我们之前所说的境界:在不同的模块中做到彻底的分离，而不是逻辑意义上的分离，是物理上的分离，也就是说在运行部署之后都可以在不停止服务器的时候直接把某些模块拿下来，其他模块的功能也不受影响。
 *      - 由此，OSGI技术将来会变得非常的重要，因为它在实现模块化解耦的路上，走得比现在大家经常所用的SSH框架走的更远。这个技术在未来大规模、高访问、高并发的Java模块化开发领域，或者是项目规范化管理中，会大大超过SSH等框架的地位。
 *
 *  2、
 *
 *《Java内存泄漏解析》：- 本章会说明什么是内存泄漏，为什么发生，以及如何防止它们。
 *  - 内存管理是Java最重要的优势之一，你只需创建对象，Java垃圾收集器会自动负责分配和释放内存。但是，情况并不那么简单，因为在Java应用程序中经常发生内存泄漏。
 *
 *  1、什么是内存泄漏？- 一个内存对象的生命周期超出了程序需要它的时间长度，我们有是也将其称为“对象游离”；
 *      - 内存泄漏的定义：应用程序不再使用的对象，垃圾收集器却无法删除它们，因为它们正在被引用。
 *      - 从图中可以看出，有被引用的对象和未被引用的对象。未引用的对象将被垃圾收集，而被引用的对象将不会被垃圾收集。未引用的对象肯定是未使用的，因为没有其他对象引用它。
 *        但是，未使用的对象并不是全部未被引用，其中一些被引用！这是内存泄漏的来源。
 *  2、为什么内存泄漏发生？
 *      - 让我们来看看下面的例子，看看为什么发生内存泄漏。在下面的例子中，对象A是指对象B。A的生命周期（t1 - t4）比B的（t2 - t3）长得多，当应用中不再使用B时，A仍然有一个B的引用，这样垃圾收集器就不能从内存中删除B。
 *        这就可能会导致内存不足的问题，因为如果A同时为更多的对象做同样的事情，那么会有很多像B这样的对象没有收集并占用内存空间。
 *      - B也可能拥有一堆其他对象的引用，B引用的对象也不会被收集。所有这些未使用的对象将消耗宝贵的内存空间。
 *  3、如何防止内存泄漏？
 *      -
 *
 *《触发JVM进行Full GC的情况及应对策略》：
 *      - Minor GC ：从年轻代空间（包括 Eden 和 Survivor 区域）回收内存被称为 Minor GC
 *      - Major GC ：对老年代GC称为Major GC
 *      - Full GC : 而Full GC是对整个堆来说的
 *  下边看看有那种情况触发JVM进行Full GC及应对策略。
 *   1、System.gc()方法的调用
 *      - 此方法的调用是建议JVM进行Full GC,虽然只是建议而非一定,但很多情况下它会触发 Full GC,从而增加Full GC的频率,也即增加了间歇性停顿的次数。
 *      - 强烈影响系建议能不使用此方法就别使用，让虚拟机自己去管理它的内存，可通过通过-XX:+ DisableExplicitGC来禁止RMI调用System.gc。
 *   2、老年代空间不足
 *      - 老年代空间只有在新生代对象转入及 创建为大对象、大数组时才会出现不足的现象，当执行Full GC后空间仍然不足，则抛出如下错误：
 *        java.lang.OutOfMemoryError: Java heap space
 *      - 为避免以上两种状况引起的Full GC，调优时应尽量做到让对象在Minor GC阶段被回收、让对象在新生代多存活一段时间及不要创建过大的对象及数组。
 *   3、永生区空间不足
 *   4、统计得到的Minor GC晋升到老年代的平均大小 大于老年代的剩余空间
 *   5、堆中分配很大的对象
 *      - 所谓大对象，是指需要大量连续内存空间的java对象，例如很长的数组，此种对象会直接进入老年代，而老年代虽然有很大的剩余空间，
 *        但是无法找到足够大的连续空间来分配给当前对象，此种情况就会触发JVM进行Full GC。
 *
 *《JVM 垃圾回收机制 - GC》:
 *  1、垃圾回收器的种类 ：大概可以分成 四种
 *      1、串行垃圾回收器（Serial Garbage Collector）
 *      2、并行垃圾回收器（Parallel Garbage Collector）
 *      3、并发标记扫描垃圾回收器（CMS Garbage Collector）- CMS 是一种以获取 最短回收停顿时间为目标 的收集器。
 *      4、G1垃圾回收器（G1 Garbage Collector）
 *
 *《Java内存模型中的happens-before 原则是什么？》
 *  1、What - 先行发生是Java内存模型中定义的两个操作之间的 偏序关系。- 如果说操作A先行发生于操作B，其实就是说在发生操作B之前，操作A产生的影响能被操作B观察到。
 *  2、背景 - 我们编写的程序都要经过优化后（编译器 和 处理器 会对我们的程序进行优化 以提高运行效率）才会被运行，
 *           优化分为很多种，其中有一种优化叫做 重排序，重排序需要遵守happens-before规则，不能说你想怎么排就怎么排，如果那样岂不是乱了套。
 *         - Java语言设计之初就引入了线程的概念，以充分利用现代处理器的计算能力，这既带来了强大、灵活的多线程机制，也带来了线程安全等令人混淆的问题，
 *           而Java内存模型(JMM)为我们提供了一个在纷乱之中达成一致的指导准则。
 *  3、部分规则如下：
 *      1、程序顺序规则：一个线程中的每个操作happens-before于该线程中的 任意后续操作.
 *          - 注：程序顺序规则中所说的每个操作happens-before于该线程中的任意后续操作并不是说前一个操作必须要在后一个操作之前执行，而是指前一个操作的执行结果必须对后一个操作可见，如果不满足这个要求那就不允许这两个操作进行重排序.
 *      2、监视器锁规则： 对一个锁的解锁，happens-before 于随后对这个锁的加锁
 *      3、volatile变量规则：对一个volatile变量的写操作先行发生于 后面对这个变量的读操作，这里的“后面”同样是指时间上的先后顺序
 *      4、线程启动规则：Thread对象的start()方法先行发生于 此线程的每一个动作。
 *      5、传递性：如果 A happens-before B,且 B happens-before C,那么A happens-before C
 *
 *
 */
public class JVMLearn {

    //bTraceTest
    public int add(int a, int b) {
        return a + b;
    }
    public void run(){
        int a = (int) (Math.random() * 1000);
        int b = (int) (Math.random() * 1000);
        System.out.println(add(a, b));
    }
    public static void main(String[] args) throws IOException {
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        JVMLearn bTraceTest=new JVMLearn();
        bReader.readLine();
        for (int i = 0; i < 10; i++) {
            bTraceTest.run();
        }
    }

}
