package com.example.concurrent.lock;

/**
 * Created by MintQ on 2018/6/25.
 *
 *
 * 《1》死锁:
 *      一个经典的多线程问题:当一个线程 永远地 持有一个锁，并且其他线程都 尝试 去获得这个锁时，那么它们将永远被阻塞，这个我们都知道。
 *                   如果线程A持有锁L并且想获得锁M，线程B持有锁M并且想获得锁L，那么这两个线程将永远等待下去，这种情况就是最简单的死锁形式。
 *      死锁是设计的BUG，问题比较隐晦。不过死锁造成的影响很少会立即显现出来，一个类可能发生死锁，并不意味着每次都会发生死锁，这只是表示有可能。
 *      当死锁出现时，往往是在最糟糕的情况----高负载的情况下。
 * 《2》
 *
 * 《3》避免死锁的方式：  既然可能产生死锁，那么接下来，讲一下如何避免死锁。
 *      1、让程序每次至多只能获得一个锁。当然，在多线程环境下，这种情况通常并不现实
 *      2、设计时考虑清楚 锁的顺序 ，尽量减少嵌在的加锁交互数量
 *      3、既然死锁的产生是两个线程无限等待对方持有的锁，那么只要等待时间有个上限不就好了。当然synchronized不具备这个功能，但是我们可以使用Lock类中的tryLock方法去尝试获取锁，这个方法可以指定一个 超时时限，在等待超过该时限之后变回返回一个失败信息
 *
 *
 * 面试题1、什么情况下导致线程死锁，遇到线程死锁该怎么解决？（代码案例说明）
 *      分析：所谓的线程死锁是指 多个线程 因竞争资源而造成的一种 僵局（即相互等待），若无外力作用，这些进程都将无法向前推进。
 *
 *
 */
public class DeadLock implements Runnable{
    public int flag = 1;
    //共享的静态对象类
    private static Object o1 = new Object(), o2 = new Object();

    @Override
    public void run() {
        System.out.println("flag = "+flag);
        if (flag == 1) {
            //同步对象o1
            synchronized (o1) {
                try {
                    //o1对象线程等待500 毫秒
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //同步对象o2
                synchronized (o2) {
                    System.out.println("1");
                }
            }
        }
        if (flag == 0) {
            //同步对象o2
            synchronized (o2) {
                try {
                    //o2线程对象等待500 毫秒
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //同步对象o1
                synchronized (o1) {
                    System.out.println("0");
                }
            }
        }

    }

    public static void main(String[] args) {
        DeadLock td1 = new DeadLock();
        DeadLock td2 = new DeadLock();
        td1.flag = 1;
        td2.flag = 0;
        //让td1,td1都处于可执行状态，但JVM先执行哪一个线程不确定
        new Thread(td1).start();
        new Thread(td2).start();
    }
    //上面代码运行后，会处于线程o1等待线程o2，线程o2又等待线程o1释放资源，所以处于相互等待的状态，即程序也就产生线程死锁。
    //那么如何解决才能避免死锁的产生呢？在有些情况下的死锁是可以避免的，两种用于避免死锁的技术：1、加锁顺序（线程按照一定的顺序加锁）。2：加锁时限（线程尝试获取锁的时候加上一定的时限，超过时限则放弃对该锁的请求，并释放自己占有的锁）
}
