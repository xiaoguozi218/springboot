package com.example.test.thread;
/**
 * @author  xiaoguozi
 * @create  2018/9/18 下午4:46
 * @desc    三个售票窗口同时出售20张票
 *
 * 程序分析：
 *     (1)票数要使用同一个静态值
 *     (2)为保证不会出现卖出同一个票数，要java多线程同步锁。
 * 设计思路：
 *     (1)创建一个站台类Station，继承Thread，重写run方法，在run方法里面执行售票操作！售票要使用同步锁：即有一个站台卖这张票时，其他站台要等这张票卖完！
 *     (2)创建主方法调用类
 *
 **/
public class Station extends Thread {

    // 通过构造方法给线程名字赋值
    public Station(String name) {
        super(name);// 给线程名字赋值
    }
    // 为了保持票数的一致，票数要静态
    static int tick = 20;
    // 创建一个静态钥匙
    static Object ob = "aa";//值是任意的
    // 重写run方法，实现买票操作
    @Override
    public void run() {
        while (tick > 0) {
            synchronized (ob) {// 这个很重要，必须使用一个锁，
                // 进去的人会把钥匙拿在手上，出来后才把钥匙拿让出来
                if (tick > 0) {
                    System.out.println(getName() + "卖出了第" + tick + "张票");
                    tick--;
                } else {
                    System.out.println("票卖完了");
                }
            }
            try {
                sleep(1000);//休息一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author  xiaoguozi
     * @create  2018/9/18 下午4:51
     * @desc    示例：三个售票窗口同时出售10张票
     **/
    public static void main(String[] args) {
        //实例化站台对象，并为每一个站台取名字
        Station station1 = new Station("窗口1");
        Station station2 = new Station("窗口2");
        Station station3 = new Station("窗口3");
        // 让每一个站台对象各自开始工作
        station1.start();
        station2.start();
        station3.start();
    }
}
