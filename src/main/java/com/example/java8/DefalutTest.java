package com.example.java8;

/**
 * Created by MintQ on 2018/7/6.
 *
 * 在接口中新增了default方法和static方法，这两种方法可以有方法体
 *
 * ~结论：接口中的static方法不能被继承，也不能被实现类调用，只能被自身调用
 * ~结论1：default方法可以被子接口继承亦可被其实现类所调用
 * ~结论2：default方法被继承时，可以被子接口覆写
 * ~结论3：如果一个类实现了多个接口，且这些接口中无继承关系，这些接口中若有相同的（同名，同参数）的default方法，则接口实现类会报错，接口实现类必须通过特殊语法指定该实现类要实现那个接口的default方法
 *
 */
public interface DefalutTest {

    static int a =5;
    default void defaultMethod(){
        System.out.println("DefalutTest defalut 方法");
    }

    int sub(int a,int b);

    static void staticMethod() {
        System.out.println("DefalutTest static 方法");
    }
}
