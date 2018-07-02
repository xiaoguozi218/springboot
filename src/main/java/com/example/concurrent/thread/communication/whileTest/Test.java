package com.example.concurrent.thread.communication.whileTest;

/**
 * Created by MintQ on 2018/7/2.
 *
 * 在这种方式下，线程A不断地改变条件，线程ThreadB不停地通过while语句检测这个条件(list.size()==5)是否成立 ，从而实现了线程间的通信。
 * 但是这种方式会浪费CPU资源。之所以说它浪费资源，是因为JVM调度器将CPU交给线程B执行时，它没做啥“有用”的工作，只是在不断地测试 某个条件是否成立。
 * 就类似于现实生活中，某个人一直看着手机屏幕是否有电话来了，而不是： 在干别的事情，当有电话来时，响铃通知TA电话来了。
 *
 * 这种方式还存在另外一个问题：
 *   轮询的条件的可见性问题，关于内存可见性问题。
 *   线程都是先把变量读取到本地线程栈空间，然后再去再去修改的本地变量。
 *   因此，如果线程B每次都在取本地的 条件变量，那么尽管另外一个线程已经改变了轮询的条件，它也察觉不到，这样也会造成死循环。
 *
 */
public class Test {
    public static void main(String[] args) {
        MyList service = new MyList();

        ThreadB b = new ThreadB(service);
        b.setName("B");
        b.start();

        ThreadA a = new ThreadA(service);
        a.setName("A");
        a.start();

    }
}
