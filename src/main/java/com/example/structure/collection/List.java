package com.example.structure.collection;

import java.util.*;

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
 *《fail-fast机制》 -
 *  - 背景： 在JDK的Collection中我们时常会看到类似于这样的话：例如，ArrayList:
 *          注意，迭代器的快速失败行为无法得到保证，因为一般来说，不可能对是否出现不同步并发修改做出任何硬性保证。快速失败迭代器会尽最大努力抛出 ConcurrentModificationException。
 *          因此，为提高这类迭代器的正确性而编写一个依赖于此异常的程序是错误的做法：迭代器的快速失败行为应该仅用于检测 bug。
 *  一、什么是fail-fast机制 -  “快速失败”也就是fail-fast，它是Java集合的一种 错误检测机制。
 *      当多个线程对集合进行结构上的改变的操作时，有可能会产生fail-fast机制。记住是有可能，而不是一定。例如：假设存在两个线程（线程1、线程2），线程1通过Iterator在遍历集合A中的元素，
 *      在某个时候线程2修改了集合A的结构（是结构上面的修改，而不是简单的修改集合元素的内容），那么这个时候程序就会抛出 ConcurrentModificationException 异常，从而产生fail-fast机制。
 *      如果单线程违反了规则，同样也有可能会抛出改异常。
 *  二、fail-fast产生原因 -
 *      从上面的源代码我们可以看出，ArrayList中无论add、remove、clear方法只要是涉及了改变ArrayList元素的个数的方法都会导致modCount的改变。
 *      所以我们这里可以初步判断由于expectedModCount 得值与modCount的改变不同步，导致两者之间不等从而产生fail-fast机制。
 *  三、fail-fast解决办法 -
 *      方案一：在遍历过程中所有涉及到改变modCount值得地方全部加上synchronized或者直接使用Collections.synchronizedList，这样就可以解决。但是不推荐，因为增删造成的同步锁可能会阻塞遍历操作。
 *      方案二：使用CopyOnWriteArrayList来替换ArrayList。推荐使用该方案。 (COW)
 *             - CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。
 *               这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
 *
 *
 */
public class List {

    public static void main(String[] args) {
        //去除List集合中的重复值（四种好用的方法）

        //1、set集合去重，不打乱顺序
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("aaa");
//        list.add("bbb");
//        list.add("aaa");
//        list.add("aba");
//        list.add("aaa");

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
//        ArrayList newList = new ArrayList(new HashSet(list));
//
//        System.out.println( "去重后的集合： " + newList);
        for(int i = 0 ; i < 10;i++){
            list.add(i);
        }
        new threadOne().start();
        new threadTwo().start();

    }

    private static ArrayList<Integer> list = new ArrayList<Integer>();


    /**
     * @desc:线程one迭代list
     * @Project:test
     * @Authro:gsh
     * @data:2014年7月26日
     */
    private static class threadOne extends Thread{
        @Override
        public void run() {
            Iterator<Integer> iterator = list.iterator();
            while(iterator.hasNext()){
                int i = iterator.next();
                System.out.println("ThreadOne 遍历:" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @desc:当i == 3时，修改list
     * @Project:test
     * @Authro:gsh
     * @data:2014年7月26日
     */
    private static class threadTwo extends Thread{
        @Override
        public void run(){
            int i = 0 ;
            while(i < 6){
                System.out.println("ThreadTwo run：" + i);
                if(i == 3){
                    list.remove(i);
                }
                i++;
            }
        }
    }

}
