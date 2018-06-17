package com.example.pattern.singleton;
/**
 * @author  xiaoguozi
 * @create  2018/6/17 下午5:00
 * @desc
 *
 *
 *
 *  注意：一、构造函数 私有化 （外部不能访问）
 *       二、单例对象。在自己内部创建一个私有的自己类 类型的成员变量。
 *       三、静态方法。一直存在，全局访问点，允许客户访问它的唯一实例。
 *
 *
 *
 **/
public class Singleton_DCL {

    //一、构造函数 私有化 （外部不能访问）
    private Singleton_DCL() {

    }

        // 1、memory = allocate() 分配对象的内存空间
        // 2、ctorInstance() 初始化对象
        // 3、instance = memory 设置instance指向刚分配的内存
    // JVM和cpu优化，发生了指令重排
        // 1、memory = allocate() 分配对象的内存空间
        // 3、instance = memory 设置instance指向刚分配的内存
        // 2、ctorInstance() 初始化对象
    // 二、单例对象
    private volatile static Singleton_DCL instance = null;
    // 三、静态方法
    public static Singleton_DCL getInstance() {
        if (instance == null) { // 双重检测机制（DCL） // B
            synchronized (Singleton_DCL.class) { // 同步锁
                if (instance == null) {
                    instance = new Singleton_DCL(); // A
                }
            }
        }
        return instance;
    }

}
