package com.example.eatuul.filter;

/**
 * Created by MintQ on 2018/6/28.
 * 接下来就是一系列Filter的代码了，先上父类EatuulFilter的源码
 */
public abstract class EatuulFilter {
    abstract public String filterType();
    abstract public int filterOrder();
    abstract public void run();
}
