package com.example.test.jvm;

/**
 * Created by MintQ on 2018/5/14.
 *
 * JVM 调优：
 *
 *  《1》虚拟机栈： 一个线程一个栈，一个方法一个栈帧 ！
 *
 *  《2》堆内存： 新生代   1、eden           （8）     --> 复制算法： 效率高！快     为什么新生代适合用复制算法？  因为new100个对象，经过第一次回收，往往会回收90个左右，这个时候剩下没有被回收的对象 比较少，所以适合用复制算法进行垃圾回收。
 *                      2、survivor from  （1）
 *                      3、survivor       （1）
 *
 *    GC 如何确定垃圾
 *      什么是垃圾？
 *      如何确定垃圾
 *          引用计数 --->   会有循环引用的问题
 *          可达性分析算法 --->    从roots对象计算可以到达的对象
 *      垃圾收集算法
 *          Mark-Sweep 标记清除  ---> 会产生 内存碎片
 *          Copying    复制     ---> 解决了 内存碎片 问题，但是浪费内存
 *          Mark-Compact 标记整理 ---> 效率比copy略低，
 *
 *      JVM采用分代算法：
 *          new: 存活对象少，使用复制算法，占用的内存空间也不大，效率也高
 *          old: 垃圾少，一般使用标记-整理算法
 *
 *
 *  JVM 垃圾收集器
 *      Serial Collector   ：XX:+UseSerialGC
 *                           单线程
 *      Parallel Collector : 并发量大，不过每次垃圾收集，JVM需要停顿
 *      CMS Collector      : 停顿时间短
 *      G1                 : 不仅停顿短，同时并发大
 *
 *  Java对象的分配：1->2->3->4
 *      1、栈上分配    ：线程私有小对象
 *                      无逃逸
 *                      支持标量替换
 *                      无需调整
 *      2、线程本地分配TLAB (Thread Local Allacation Buffer) ：占用Eden，默认1%
 *                                                          多线程的时候不用竞争Eden就可以申请空间，提高效率
 *                                                          小对象
 *                                                          无需调整
 *      3、老年代     ：大对象
 *      4、eden      ：
 *
 *  JVM 参数
 *      -   标准参数，所有JVM都应该支持
 *      -X  非标，每个JVM实现都不同
 *      -XX 不稳定参数，下一个版本可能会取消
 *  常用参数设置：
 *      堆设置
 *          -Xms:初始堆大小
 *          -Xmx:最大堆大小
 *          -Xss：线程栈大小
 *          -XX:NewSize=n:设置年轻代大小
 *          -XX:NewRatio=n:设置年轻代和年老代的比值。比如：3，表示年轻代：年老代比值为1：3，年轻代占整个年轻代年老代的1/4
 *          -XX：SurvivorRatio=n:年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
 *          -XX:MaxPermSize=n:设置持久代大小
 *      收集器设置
 *          -XX:+UseSerialGC:设置串行收集器
 *          -XX:+UseParallelGC:设置并行收集器
 *          -XX:+UseConcMarkSweepGC:设置并发收集器
 *      垃圾回收统计信息
 *          -XX:+PrintGC
 *          -XX:+PrintGCDetails
 *          -Xloggc:filename
 *
 *
 *  典型tomcat优化配置
 *      set JAVA_OPTS=
 *          -Xms4g
 *          -Xmx4g
 *          -Xss512k
 *          -XX:+AggressiveOpts     （进攻性的优化）
 *          -XX:+UseBiasedLocking   （启用偏向锁）
 *          -XX:PermSize=64M
 *          -XX:MaxPermSize=300M
 *          -XX:+DisableExplicitGC
 *          -XX:+UseConcMarkSweepGC     使用CMS缩短响应时间，并发收集，低停顿
 *          -XX:+UseParNewGC            并行收集新生代的垃圾
 *          -XX:+CMSParallelRemarkEnabled   在使用UseParNewGC的情况下，尽量减少mark的时间
 *
 *
 *
 */
public class JVMTest {

    public static int method() {
        return 1;
    }

    public static void main(String[] args) {
        String m = method() + "";
        String m1 = method() + "";
        System.out.println(m == m1);

        String m2 = 1 + "";
        String m3 = 1 + "";
        System.out.println(m2 == m3);

        int a = 1;
        String m4 = a + "";
        String m5 = a + "";
        System.out.println(m4 == m5);

        final int b = 1;
        String m6 = b + "";
        String m7 = b + "";
        System.out.println(m6 == m7);
    }


}
