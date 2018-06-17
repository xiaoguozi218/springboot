package com.example.pattern.singleton;
/**
 * @author  xiaoguozi
 * @create  2018/6/17 下午5:25
 * @desc    饿汉式
 *
 *  单例模式：是一种常用的软件设计模式，在它的核心结构中值包含一个被称为单例的特殊类。一个类只有一个实例，即一个类只有一个对象实例。
 *          对于系统中的某些类来说，只有一个实例很重要，例如，一个系统中可以存在多个打印任务，但是只能有一个正在工作的任务；售票时，一共有100张票，可有有多个窗口同时售票，但需要保证不要超售（这里的票数余量就是单例，售票涉及到多线程）。如果不是用机制对窗口对象进行唯一化将弹出多个窗口，如果这些窗口显示的都是相同的内容，重复创建就会浪费资源。
 *
 *  应用场景（来源：《大话设计模式》）：
 *          需求：在前端创建工具箱窗口，工具箱要么不出现，出现也只出现一个
 * 　　　　     遇到问题：每次点击菜单都会重复创建“工具箱”窗口。
 * 　　　　     解决方案一：使用if语句，在每次创建对象的时候首先进行判断是否为null，如果为null再创建对象。
 *          需求：如果在5个地方需要实例出工具箱窗体
 * 　　　　     遇到问题：这个小bug需要改动5个地方，并且代码重复，代码利用率低
 * 　　　　     解决方案二：利用单例模式，保证一个类只有一个实例，并提供一个访问它的全局访问点。
 *
 *  通过以下几种方式，我们会发现，所有的单例模式都是使用静态方法进行创建的，所以单例对象在内存中 静态共享区 中存储。
 *
 *  单例模式可以分为 饿汉式 和 懒汉式：
 * 　　　　饿汉式单例模式：在类加载时就完成了初始化，所以类加载比较慢，但获取对象的速度快。
 * 　　　　懒汉式单例模式：在类加载时不初始化。
 *
 *
 *
 *
 *
 **/
public class Singleton_hungry {

    //  饿汉 （一般的）    这种方式基于classloder机制避免了多线程的同步问题，不过，instance在类装载时就实例化，这时候初始化instance显然没有达到lazy loading的效果。
//    private static Singleton_hungry instance = new Singleton_hungry();
//    private Singleton_hungry(){
//    }
//    public static Singleton_hungry getInstance(){
//        return instance;
//    }



    //  饿汉-变种       表面上看起来差别挺大，其实和第三种方式（一般的饿汉）差不多，都是在类初始化即实例化instance
    private static Singleton_hungry instance = null;
    static{
        instance = new Singleton_hungry();
    }
    private Singleton_hungry(){}
    public static Singleton_hungry getInstance(){
        return instance;
    }
}
