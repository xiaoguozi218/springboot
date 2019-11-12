package com.example.java8;

/***
 *
 * Happens-Before 原则，在 《JSR 133 ：Java内存模型与线程规范》中，Happens-Before 内存模型被定义成 Java 内存模型近似模型，
 * Happens-Before 原则要说明的是关于可见性的一组偏序关系。
 * - Happens-Before 原则一共包括 8 条，下面我们一起简单的学习一下这 8 条规则
 *  1、程序顺序规则
 *  2、锁定规则
 *  3、volatile变量规则
 *  4、线程启动规则
 *  5、线程结束规则
 *  6、中断规则
 *  7、终结器规则
 *  8、传递性规则
 *
 */
public class HappensBeforeLearn {

    /***
     * 4、线程启动规则:
     * - 这条规则是指主线程 A 启动子线程 B 后，子线程 B 能够看到主线程在启动子线程 B 前的操作。
     *   子线程 t1 能够看见主线程对 count 变量的修改，所以在线程中打印出来的是 12 。这也就是线程启动规则
     */
    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println(count);
        });
        count = 12;
        t1.start();
    }

}
