package com.example.concurrent.mq;

/**
 * Created by MintQ on 2018/5/9.
 */
public class Product {
    private int id;


    public Product(int id ) {
        this.id=id;
    }

    @Override
    public String toString() {  //重写toString方法
        return "产品：" +this.id;
    }
}
