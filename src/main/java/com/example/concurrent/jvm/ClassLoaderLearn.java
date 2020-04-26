package com.example.concurrent.jvm;
/**
 * 类加载学习
 * @author  gsh
 * @date  2019/12/23 下午6:27
 * 1. 什么是类的加载？
 * 2. 哪些情况会触发类的加载？
 * 3. 讲一下JVM加载一个类的过程
 * 4. 什么时候会为变量分配内存？
 * 5. JVM的类加载机制是什么？
 * 6. 双亲委派机制可以打破吗？为什么
 *
 *
 * 原理：
 * 1、解析阶段：就是jvm将常量池的符号引用替换为直接引用。
 *  符号引用和直接引用
 * 假设有一个Worker类，包含了一个Car类的run()方法，像下面这样：
 * class Worker{
 *         public void gotoWork(){
 *             car.run(); //这段代码在Worker类中的二进制表示为符号引用
 *         }
 *     }
 * 在解析阶段之前，Worker类并不知道car.run()这个方法内存的什么地方，于是只能用一个字符串来表示这个方法。该字符串包含了足够的信息，比如类的信息，方法名，方法参数等，以供实际使用时可以找到相应的位置。
 * 这个字符串就被称为符号引用。
 * 在解析阶段，jvm根据字符串的内容找到内存区域中相应的地址，然后把符号引用替换成直接指向目标的指针、句柄、偏移量等，这之后就可以直接使用了。
 * 这些直接指向目标的指针、句柄、偏移量就被成为直接引用。
 *
 **/
public class ClassLoaderLearn {



}