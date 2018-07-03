package com.example.pattern.interface_abstract;

/**
 * Created by MintQ on 2018/7/3.
 *
 * Java抽象类与接口的区别
 *  1、接口只能包含抽象方法，抽象类可以包含普通方法。
 *  2、接口不包含构造方法，抽象类里可以包含构造方法（但是抽象类不能被实例化）。
 *  3、如果许多类实现了某个接口，那么每个都要用代码实现接口中的抽象方法
 *  4、如果某一些类的实现有共通之处，则可以抽象出来一个抽象类，让抽象类实现接口的公用的代码，而那些个性化的方法则由各个子类去实现。
 *  5、接口只能定义静态常量属性，抽象类既可以定义普通属性，也可以定义静态常量属性。
 *  6、接口是多实现（implements），抽象类是单继承（extends）。
 *
 *  所以，抽象类是为了简化接口的实现，他不仅提供了公共方法的实现，让你可以快速开发，又允许你的类完全可以自己实现所有的方法。
 *
 */
public class Interface_Abstract_Learn {
}
