package com.example.proxy.jdk_dynamic;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by MintQ on 2018/6/12.
 *
 * 添加一个代理JDKProxy,该代理实现InvocationHandler接口且覆写invoke方法。
 */
public class JDKDynmProxy implements InvocationHandler {

    private Object object;

    public Object getInstance(Object object) {
        this.object = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start ---- jdk-dongtai");
        method.invoke(object,args);
        System.out.println("end ---- jdk-dongtai");
        return null;
    }
}
