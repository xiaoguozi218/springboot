package com.example.annotation.annoAndreflect;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 「Java」利用注解和反射实现一个“低配版”的依赖注入
 * @author  gsh
 * @date  2018/11/5 下午5:09
 *
 * 在Spring中，我们可以通过 @Autowired注解的方式为一个方法中注入参数，
 * 那么这种方法背后到底发生了什么呢，这篇文章将讲述如何用Java的注解和反射实现一个“低配版”的依赖注入。
 * 下面是我们要做的一些事情：
 *
 * 通过 @interface的方式定义一个注解
 * 为某个希望杯被注入的方法添加这个注解
 * 编写测试代码，通过反射获取添加了注解的方法对应的Method对象，将该方法对象设置为可访问的，通过反射创建对象并调用这个方法，同时注入依赖数据
 *
 * 如上所述，我们分为三个步骤， 去加工出这个低配版的依赖注入，下面就来讲讲每一步的详细步骤
 *
 * 我们要编写的代码的结构分为三部分：
 *
 * Autowired: 声明的注解
 * Demo类：含有被依赖注入的方法setStr
 * Test类：通过反射获取被Autowired注解的方法，并进行依赖注入
 *
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
