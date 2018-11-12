package com.example.concurrent.jvm;

/**
 * 深入理解 Java final 变量的内存模型
 * 对于 final 域，编译器和处理器要遵守两个重排序规则：
 *  - 在构造函数内对一个 final 域的写，与随后把这个构造对象的引用赋值给一个变量，这两个操作之间不能重排序
 *  - 初次读一个包含 final 域的对象的引用，与随后初次读这个 final 域，这两个操作之间不能重排序
 *
 * 写 final 域的重排序规则 - 在写 final 域的时候有两个规则：
 *  - JMM 禁止编译器把 final 域的写重排序到构造函数之外
 *  - 编译器会在 final 域的写之后，构造函数 return 之前，插入一个 StoreStore 屏障，这个屏障禁止处理器把 final 域的写重排序到构造函数之外。
 *
 * 读 final 域的重排序规则 - 读 final 域的重排序规则如下:
 *  - 在一个线程中,初次读对象引用与初次读该对象包含的 final 域,JMM 禁止处理器重排序这两个操作(注意,这个规则仅仅针对处理器)。
 *  编译器会在读 final 域操作的前面插入一个 LoadLoad 屏障。
 */
public class FinalLearn {
}
