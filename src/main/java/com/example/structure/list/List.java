package com.example.structure.list;

/**
 * Created by MintQ on 2018/6/12.
 *
 * 本文将介绍下面List子类的一些细节：
    1、ArrayList
    2、Vector和Stack
    3、LinkedList
    4、SynchronizedList
 *
 *  ArrayList的细节：细节1：ArrayList基于数组实现，访问元素效率快，插入删除元素效率慢
 *                  细节2：ArrayList支持快速随机访问
 *                  细节3：大多数情况下，我们都应该指定ArrayList的初始容量. (ArrayList每次扩容至少为原来容量大小的1.5倍，其默认容量是10，当你不为其指定初始容量时，它就会创建默认容量大小为10的数组)
 *
 *  Vector和Stack的细节: 细节1：Vector也是基于数组实现，同样支持快速访问，并且线程安全
 *                      细节2：Vector的扩容机制不完善
 *                      细节3：Stack继承于Vector，在其基础上扩展了栈的方法
 *
 *  LinkedList的细节: 细节1：LinkedList基于链表实现，插入删除元素效率快，访问元素效率慢
 *                   细节2：LinkedList可以当作队列和栈来使用
 *
 *  SynchronizedList的细节: 细节1：SynchronizedList继承于SynchronizedCollection，使用装饰者模式，为原来的List加上锁，从而使List同步安全
 *
 *  最后，我们来做一次总结：
 *      ArrayList和LinkedList适用于不同使用场景，应根据具体场景从优选择
 *      根据ArrayList的扩容机制，我们应该开始就指定其初始容量，避免资源浪费
 *      LinkedList可以当作队列和栈使用，当然我们也可以进一步封装
 *      尽量不使用Vector和Stack，同步场景下，使用SynchronizedList替代
 *
 *
 */
public class List {


}
