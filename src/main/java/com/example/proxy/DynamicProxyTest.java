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
 * 在原始类和接口还未知的时候 ，确定代理类的代理行为，也就是让代理类与原始类脱离了直接联系.
 *
 *
 *
 * 动态代理有两个主要应用：
 * 1、第一是如果代码中多处会用到相同的代码，如安全性检查，权限的验证（AOP），在很多业务模块都会用到，一般都是放在我们业务代码的头部或者是尾部，那么如果不用动态代理的话，这些相同的代码就会分散在你程序的各个部分，首先代码复用率太差，其次是可维护性不高。
 * 2、第二个主要应用，是类似拦截器的一种应用，拦截器就是在你业务代码前，加上检查性代码，如果通过则执行你的业务代码，如果不通过则不执行你的业务代码。
 *
 *
 * 注意：
 * 1、 java jdk动态代理其实是字节码生成技术与反射机制的一个结合
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


    static class DynamicProxy implements InvocationHandler {    //这部分代码需要我们自己实现InvocationHandler接口

        Object oriObj;  // 被代理类的实例

        Object bind(Object oriObj) {
            this.oriObj = oriObj;
            return Proxy.newProxyInstance(oriObj.getClass().getClassLoader(),oriObj.getClass().getInterfaces(),this);
            //Proxy的静态方法，newProxyInstance（），这个方法有三个参数，
            // 第一个参数是被代理类的类加载器（classLoader），
            // 第二个参数是被代理类的所有接口，
            // 第三个参数是我们上面实现类（InvocationHandler）的实例，
            // 那么这个方法，返回的就是一个代理类，就是在这个方法中，动态拼接处代理类的字节码。
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {     //重写 其中的invoke方法
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

