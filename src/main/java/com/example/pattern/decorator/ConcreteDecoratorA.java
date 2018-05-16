package com.example.pattern.decorator;

/**
 * Created by MintQ on 2018/5/16.
 *
 * 4、再定义装饰器的实现者：
 */
public class ConcreteDecoratorA extends Decorator{

    @Override
    public void operation() {
        System.out.println("加上装饰功能A.....");
        super.operation();
    }
}
