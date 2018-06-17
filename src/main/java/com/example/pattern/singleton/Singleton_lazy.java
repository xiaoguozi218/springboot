package com.example.pattern.singleton;
/**
 * @author  xiaoguozi
 * @create  2018/6/17 下午5:39
 * @desc    第一种（懒汉，线程不安全）: 这种写法lazy loading很明显，但是致命的是在多线程不能正常工作。
 *          第二种（懒汉，线程安全）：这种写法在getInstance()方法中加入了synchronized锁。能够在多线程中很好的工作，而且看起来它也具备很好的lazy loading，但是效率很低（因为锁），并且大多数情况下不需要同步。
 *
 *
 *
 **/
public class Singleton_lazy {
    //第一种（懒汉，线程不安全）
//    private static Singleton_lazy instance;
//    private Singleton_lazy(){}
//    public static Singleton_lazy getInstance(){
//        if (instance == null) {
//            instance = new Singleton_lazy();
//        }
//        return instance;
//    }


    //  第二种（懒汉，线程安全）
    private static Singleton_lazy instance;
    private Singleton_lazy(){}
    public static synchronized Singleton_lazy getInstance(){    //这种写法在getInstance()方法中加入了 synchronized 锁。
        if (instance == null) {
            instance = new Singleton_lazy();
        }
        return instance;
    }

}
