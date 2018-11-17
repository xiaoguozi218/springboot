package com.example.pattern.factory;

/**
 * Created by Administrator on 2018/11/17.
 */
public class Dog implements Animal { // 定义子类Dog

    @Override
    public void say() { // 覆写say()方法
        System.out.println("我是小狗，汪汪！");
    }
}
