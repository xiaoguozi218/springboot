package com.example.java8;

/**
 * Created by MintQ on 2018/7/6.
 *
 * 接口里的静态方法，即static修饰的有方法体的方法不会被继承或者实现，但是静态变量会被继承
    例如：我们添加一个接口DefalutTest的实现类DefaultTestImpl
 */
public class DefaultTestImpl implements DefalutTest{

    @Override
    public int sub(int a, int b) {
        return a-b;
    }
}
