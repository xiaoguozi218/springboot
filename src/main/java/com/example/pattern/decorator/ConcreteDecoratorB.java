package com.example.pattern.decorator;

/**
 * Created by MintQ on 2018/5/16.
 */
public class ConcreteDecoratorB extends Decorator {

    @Override
    public void operation() {
        System.out.println("加上装饰功能B...");
        super.operation();
    }
}
