package com.example.proxy;

/**
 * Created by MintQ on 2018/6/12.
 *
 * 总结：
 *      1、jdk静态代理类只能为一个被代理类服务，如果需要代理的类比较多，那么会产生过多的代理类。jdk静态代理在编译时产生class文件，运行时无需产生，可直接使用，效率好。
 *      2、jdk动态代理必须实现接口，通过反射来动态代理方法，消耗系统性能。但是无需产生过多的代理类，避免了重复代码的产生，系统更加灵活。
 *      3、cglib动态代理无需实现接口，通过生成子类字节码来实现，比反射快一点，没有性能问题。但是由于cglib会继承被代理类，需要重写被代理方法，所以被代理类不能是final类，被代理方法不能是final。
 *
 *
 */
public class ProxyLearn {


}
