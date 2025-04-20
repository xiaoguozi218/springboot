package com.example.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by MintQ on 2018/7/5.
 *
 * LRU 全称是Least Recently Used，即 最近最少使用 的意思。
 *
 * 1、LRU算法的设计原则是：如果一个数据在最近一段时间没有被访问到，那么在将来它被访问的可能性也很小。也就是说，当限定的空间已存满数据时，应当把最少被访问到的数据淘汰。
 *
 * 2、实现LRU：
 *      ~1.用一个 数组 来存储数据，给每一个数据项标记一个访问时间戳，每次插入新数据项的时候，先把数组中存在的数据项的时间戳自增，并将新数据项的时间戳置为0并插入到数组中。
 *          每次访问数组中的数据项的时候，将被访问的数据项的时间戳置为0。当数组空间已满时，将时间戳最大的数据项淘汰。
 *      ~2.利用一个 链表 来实现，每次新插入数据的时候将新数据插到链表的头部；每次缓存命中（即数据被访问），则将数据移到链表头部；那么当链表满的时候，就将链表尾部的数据丢弃。
 *      ~3.利用 链表 和 hashmap。当需要插入新的数据项的时候，如果新数据项在链表中存在（一般称为命中），则把该节点移到链表头部，如果不存在，则新建一个节点，放到链表头部，
 *          若缓存满了，则把链表最后一个节点删除即可。在访问数据的时候，如果数据项在链表中存在，则把该节点移到链表头部，否则返回-1。这样一来在链表尾部的节点就是最近最少访问的数据项。
 *   ~对于第一种方法，需要不停地维护数据项的访问时间戳，另外，在插入数据、删除数据以及访问数据时，时间复杂度都是O(n)。对于第二种方法，链表在定位数据的时候时间复杂度为O(n)。
 *    所以在一般使用第三种方式来是实现LRU算法。
 *
 * 3、实现方案：
 *      ~使用LinkedHashMap实现：
 *          LinkedHashMap底层就是用的HashMap加双链表实现的，而且本身已经实现了按照访问顺序的存储。此外，LinkedHashMap中本身就实现了一个方法removeEldestEntry用于判断是否需要移除最不常读取的数，
 *          方法默认是直接返回false，不会移除元素，所以需要重写该方法。即当缓存满后就移除最不常用的数。
 *
 */
public class LRUCache extends LinkedHashMap<Integer, Integer> {
    //定义缓存的容量
    private int capacity;
    //带参数的构造器
    public LRUCache(int capacity){
        //调用LinkedHashMap的构造器，传入以下参数
        //accessOrder false 基于插入顺序 true 基于访问顺序（get一个元素后，这个元素被加到最后，使用了LRU 最近最少被使用的调度算法）
        super(16,0.75f,true);
        //传入指定的缓存最大容量
        this.capacity=capacity;
    }

    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }


    //实现LRU的关键方法，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
    @Override
    public boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest){
        return size()>capacity;
    }


    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // 返回 1

        cache.put(3, 3); // 该操作会使得键 2 作废
        System.out.println(cache.get(2)); // 返回 -1 (未找到)

        cache.put(4, 4); // 该操作会使得键 1 作废
        System.out.println(cache.get(1)); // 返回 -1 (未找到)
        System.out.println(cache.get(3)); // 返回 3
        System.out.println(cache.get(4)); // 返回 4
    }
}

