package com.example.pattern.factory;

/**
 * Created by Administrator on 2018/11/17.
 */
public class Cat implements Animal{ // 定义子类Cat
    @Override
    public void say() { // 覆写say()方法
        System.out.println("我是猫咪，喵呜！");
    }
}
