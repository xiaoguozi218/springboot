package com.example.pattern.singleton;

import org.junit.runner.notification.RunListener;

/**
 * @author  xiaoguozi
 * @create  2018/6/17 下午5:19
 * @desc    枚举模式：最安全
 *
 *
 **/
public class Singleton_Enum {

    // 私有构造函数
    private Singleton_Enum() {
    }
    // 静态获取实例的方法
    public static Singleton_Enum getInstance() {
        return Singleton.INSTANCE.getInstance();
    }
    // 枚举对象
    private enum Singleton {
        INSTANCE;
        private Singleton_Enum singleton;
        // JVM保证这个方法绝对只调用一次
        Singleton() {
            singleton = new Singleton_Enum();
        }
        public Singleton_Enum getInstance() {
            return singleton;
        }
    }
}
