package com.example.structure.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
 *《LinkedList 源码解读 》: - LinkedList是一个实现了List接口和Deque接口的双向链表。
 *  1、源码解读：
 *     - LinkedList内部是一个双端链表结构，有两个变量，first指向链表头部，last指向链表尾部。
 *     - Node类LinkedList的 静态内部类。
 *     - 添加操作 ：因为LinkedList即实现了List接口，又实现了Deque接口，所以LinkedList既可以添加将元素添加到尾部，也可以将元素添加到指定索引位置，还可以添加添加整个集合；
 *                 另外既可以在头部添加，又可以在尾部添加。下面我们分别从List接口和Deque接口分别介绍。
 *
 *  2、LinkedList不是线程安全的，如果想使LinkedList变成线程安全的，可以使用如下方式：
 *      List list=Collections.synchronizedList(new LinkedList(...));
 *
 *  注意：iterator()和listIterator()返回的迭代器都遵循fail-fast机制。
 *
 *
 */
public class List {

    public static void main(String[] args) {
        //去除List集合中的重复值（四种好用的方法）

        //1、set集合去重，不打乱顺序
        ArrayList<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("aaa");
        list.add("aba");
        list.add("aaa");

//        Set set = new HashSet();
//        ArrayList newList = new ArrayList();
//        for (String cd:list) {
//            if(set.add(cd)){
//                newList.add(cd);
//            }
//        }
        //2、遍历后判断赋给另一个list集合
//        ArrayList<String> newList = new  ArrayList<String>();
//        for (String cd:list) {
//            if(!newList.contains(cd)){
//                newList.add(cd);
//            }
//        }

        //3、set去重
//        Set set = new  HashSet();
//        ArrayList newList = new  ArrayList();
//        set.addAll(list);
//        newList.addAll(set);

        //4、set去重(缩减为一行)
        ArrayList newList = new ArrayList(new HashSet(list));

        System.out.println( "去重后的集合： " + newList);
    }

}
