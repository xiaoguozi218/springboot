package com.example.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by gsh on 2018/5/18.
 *
 *
 * java中的javac命令就是字节码生成的“老祖宗”，并且它也是用java写的。还有Web 中的jsp编译器，编译时植入的AOP框架，还有很常用的动态代理，甚至在反射时JVM也有可能在运行时生成字节码来提高速度.

 　　如果使用过Spring来做Bean的管理 ，那么就使用过动态代理，因为如果 Bean是面向接口的编程，那么在Spring内部都是通过 动态代理的方法来对Bean进行增强的。
 *
 *
 *  动态代理优势：
 * 不在于少写代码，而在于，
 * 在原始类和接口还未知的时候 ，确定代理类的代理行为，也就是让代理类与原始类脱离了直接联系
 *
 */
public class DynamicProxyTest {

    interface IHello {
        void sayHello();
    }

    static class Hello implements IHello {
        @Override
        public void sayHello() {
            System.out.println("hello world!");
        }
    }


    static class DynamicProxy implements InvocationHandler {

        Object oriObj;

        Object bind(Object oriObj) {
            this.oriObj = oriObj;
            return Proxy.newProxyInstance(oriObj.getClass().getClassLoader(),oriObj.getClass().getInterfaces(),this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("welcome  to ");
            return method.invoke(oriObj,args);
        }
    }


    public static void main(String[] args) {

        /**
         * 返回了一个实现了IHello接口并代理了new Hello()实例行为的对象，进行了验证，优化等
         * 生成了字节码，并进行了类的显式加载
         */
        IHello hello = (IHello) new DynamicProxy().bind(new Hello());
        hello.sayHello();
    }
}

