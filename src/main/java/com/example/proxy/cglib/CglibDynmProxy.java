package com.example.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by MintQ on 2018/6/12.
 *
 * Cglib 动态代理：
 *
 */
public class CglibDynmProxy implements MethodInterceptor{

    //可以接受任意一种参数作为被代理类，实现了动态代理
    public Object getInstance(Class<?> classz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classz);
        enhancer.setCallback(this);
        //代理的创建
        Object proxyObj = enhancer.create();
        return proxyObj;    //返回代理对象
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object proxy = null;
        System.out.println("start ---- cglib 代理");
        proxy = methodProxy.invokeSuper(o,objects);
        System.out.println("end ---- cglib 代理");
        return proxy;
    }

}
