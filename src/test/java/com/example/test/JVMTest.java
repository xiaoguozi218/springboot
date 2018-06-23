package com.example.test;

/**
 * Created by MintQ on 2018/5/14.
 *
 * JVM 调优：
 *
 *  《1》虚拟机栈： 一个线程一个栈，一个方法一个栈帧 ！
 *
 *  《2》堆内存： 新生代   1、eden           （8）     --> 复制算法： 效率高！快     为什么新生代适合用复制算法？  因为new100个对象，经过第一次回收，往往会回收90个左右，这个时候剩下没有被回收的对象 比较少，所以适合用复制算法进行垃圾回收。
 *                  2、survivor from  （1）
 *                  3、survivor       （1）
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
 *  JVM 参数
 *      -   标准参数，所有JVM都应该支持
 *      -X  非标，每个JVM实现都不同
 *      -XX 不稳定参数，下一个版本可能会取消
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
