package com.example.pattern.decorator;

/**
 * Created by MintQ on 2018/5/16.
 *
 * 2、再实现这个抽象组件的功能
 */
public class ConcreteComponent implements Component{


    @Override
    public void operation() {
        System.out.println("具体抽象组件的功能！");
    }
}
