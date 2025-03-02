package com.example.structure.map;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;


/**
 * Created by gsh on 2018/7/18.
 *
 *《HashMap 原理详解》：
 *  - HashMap是什么 - HashMap是Java常用的用来储存键值对的数据结构，它不是线程安全的 - 可以储存null键值。
 * 一、实现原理
 *  - HashMap采用 数组+链表 的方式来储存键值对，键值对的对象实现如下：通过一个Entry的数组table就实现了多个对象的保存，使用哈希值和键值解决了在插入和查找时的冲突。
 *
 *  1、put方法，写入键值对
 *
 *  2、get方法
 *
 *  3、HashMap为什么线程不安全？如何使hashmap线程安全？
 *   举例说明：
 *   - hashmap在调用put方法时会调用上面方法，假若现在同时有A线程和B线程同时获得了同一个头节点并对其进行修改，这样A写入头节点之后B也写入头节点则会将A的操作覆盖
 *   - 这上面是删除操作，同样的如果一个头节点的元素同时被两个线程删除，则又会造成混乱。
 *
 *   如何使hashmap线程安全？ -
 *      1、使用 Collections.synchronizedMap 方法来“包装”该映射。 - Map m = Collections.synchronizedMap(new HashMap(…));
 *
 *
 *  4、一些需要注意的点：
 *   - 由HashMap的原理可知，主要的储存依赖hash值的计算，因此选用String，Integer这些类做为键会提高HashMap的效率，
 *     因为String等对象一旦放入Map中就 不会发生变化，因此其hash值也不会发生改变，获取对象的速度将大大提高。
 *   - 如果HashMap的大小超过了负载因子定义的容量，HashMap将会创建一个原来两倍的bucket数组，将原来的对象放入新的数组中，扩大hashMap的容量。（负载因子初始0.75）
 *   - 在多个线程同时发现HashMap的大小过小时，都会尝试调整大小，会造成条件竞争。
 *   - 在Java 8中，如果hash相同的key的数量大于8，会使用 平衡树 代替链表。
 *   - 源码中进行红黑树的查找时，会反复用到以下两条规则：1）如果目标节点的hash值小于p节点的hash值，则向p节点的左边遍历；否则向p节点的右边遍历。
 *      2）如果目标节点的key值小于p节点的key值，则向p节点的左边遍历；否则向p节点的右边遍历。这两条规则是利用了红黑树的特性（左节点<根结点<右节点）。
 *
 *  5、在Java 8中，为什么HashMap链表长度超过8会转成树结构？ - HashMap在JDK1.8及以后的版本中引入了红黑树结构，若桶中链表元素个数大于等于8时，链表转换成树结构；
 *      若桶中链表元素个数小于等于6时，树结构还原成链表。因为红黑树的平均查找长度是 log(n)，长度为8的时候，平均查找长度为3，如果继续使用链表，平均查找长度为8/2=4，这才有转换为树的必要。
 *      链表长度如果是小于等于6，6/2=3，虽然速度也很快的，但是转化为树结构和生成树的时间并不会太短。
 *      -
 *  6、总结：
 *     - HashMap的底层是个Node数组（Node<K,V>[] table），在数组的具体索引位置，如果存在多个节点，则可能是以 链表 或 红黑树 的形式存在。
 *     - HashMap的默认初始容量（capacity）是16，capacity必须为2的幂次方；默认负载因子（load factor）是0.75；
 *     - HashMap在触发扩容后，阈值会变为原来的2倍，并且会进行重hash，重hash后索引位置index的节点的新分布位置最多只有两个：原索引位置或原索引+oldCap位置。
 *          例如capacity为16，索引位置5的节点扩容后，只可能分布在新报索引位置5和索引位置21（5+16）。
 *     - HashMap在JDK1.8之后不再有死循环的问题，JDK1.8之前存在死循环的根本原因是在扩容后同一索引位置的节点顺序会反掉。
 *
 * 《ConcurrentHashMap 原理详解》：
 *   1、内部结构：- 整个Hash表
 *              - segment(段) - 每个segment就相当于一个HashTable
 *                            - segment类继承于ReentrantLock 类
 *              - HashEntry(节点) - key : final key;
 *                               - value : volatile V value;
 *                               - hash : final int hash;
 *                               - next : final HashEntry<K,V> next;
 *
 *   2、ConcurrentHashMap在jdk1.8中主要做了2方面的改进:
 *      - 取消segments字段，直接采用transient volatile Node<K,V>[] table;保存数据，采用table数组元素作为锁，从而实现了对每一行数据进行加锁，进一步减少并发冲突的概率。
 *          - Node - Node是最核心的内部类，它包装了key-value键值对，所有插入ConcurrentHashMap的数据都包装在这里面。
 *      - 将原先table数组＋单向链表的数据结构，变更为table数组＋单向链表＋红黑树的结构。
 *          - 对于hash表来说，最核心的能力在于将key hash之后能均匀的分布在数组中。如果hash之后散列的很均匀，那么table数组中的每个队列长度主要为0或者1。
 *            但实际情况并非总是如此理想，虽然ConcurrentHashMap类默认的加载因子为0.75，但是在数据量过大或者运气不佳的情况下，还是会存在一些队列长度过长的情况，
 *            如果还是采用单向列表方式，那么查询某个节点的时间复杂度为O(n)；因此，对于个数超过8(默认值)的列表，jdk1.8中采用了红黑树的结构，那么查询的时间复杂度可以降低到O(logN)，可以改进性能。
 *
 *  3、它的高并发性主要来自于三个方面：
 *      - segment - 用分段锁实现多个线程间的 更深层次的共享访问。使用分段锁，减少了请求同一个锁的频率。
 *      - volatile - 通过对同一个 Volatile 变量的读 / 写访问，协调不同线程间读 / 写操作的内存可见性。
 *      - final - 用 HashEntry 对象的不变性来降低执行读操作的线程在遍历链表期间对加锁的需求。
 *
 *     ConcurrentHashMap 相比于 HashTable 和用同步包装器包装的 HashMap（Collections.synchronizedMap(new HashMap())）的优势：ConcurrentHashMap 拥有更高的并发性。
 *          - 在 HashTable 和由同步包装器包装的 HashMap 中，使用一个 全局的锁 来同步不同线程间的并发访问。
 *            同一时间点，只能有一个线程持有锁，也就是说在同一时间点，只能有一个线程能访问容器。这虽然保证多线程间的安全并发访问，但同时也导致对容器的访问变成串行化的了，性能不高
 *
 *     在使用锁来协调多线程间并发访问的模式下，减小对锁的竞争可以有效提高并发性。有两种方式可以减小对锁的竞争：
 *          - 减少请求 同一个锁的 频率。
 *          - 减少持有锁的 时间。
 *  4、- ConcurrentHashMap 在默认并发级别会创建包含 16 个 Segment 对象的数组。
 *
 *《访问单向链表和顺序数组第i个元素的时间复杂度的分析》：
 *  1、链表
 *      假设链表L有n个元素，如果这n个元素中每一个元素被访问的概率都为相同，则访问到第i个元素所前移的指针的平均次数为：
 *          Average = （1+2+3+......+n）/n = n(1+n)/2/n = n/2 + 1/2
 *          所以，访问第i个元素的平均时间为O(n)。 （n趋近与无穷大时）
 *  2、数组 - 在数组L中访问第i个元素的平均时间为 O(1)。  （常数级）
 *
 *
 *
 * 注意：由于只能在表头插入，所以链表中节点的顺序和插入的顺序相反。
 *
 */
public class HashMapLearn {

    /**
     *      static class Entry<K,V> implements Map.Entry<K,V> {
                 final K key;
                 V value;
                 Entry<K,V> next;
                 final int hash;
                ……
            }
     */

//    public V put(K key, V value){
//        //如果 key 为 null，调用 putForNullKey 方法写入null键的值
//        if (key == null){
//            return putForNullKey(value);
//        }
//         //根据 key 的 keyCode 计算 Hash 值
//        int hash = hash(key.hashCode());
//        //查找hash值在table中的索引
//        int i = indexFor(hash, table.length);
//        // 如果 i 索引处的 Entry 不为 null，通过循环不断遍历链表查找是否在链表中有相同key的Entry
//        for (Entry<K,V> e = tablei; e != null; e = e.next) {
//            Object k;
//            //找到与插入的值的key和hash相同的Entry
//            if (e.hash == hash && ((k = e.key) == key|| key.equals(k)){ 
//                //key值相同时直接替换value值，跳出函数
//                V oldValue = e.value;
//                e.value = value;     e.recordAccess(this);   return oldValue;         }     }
//        // 如果 i 索引处的 Entry 为 null 或者key的hash值相同而key不同  ，则需要新增Entry
//        modCount++;
//        // 将 key、value 添加到 i 索引处
//        addEntry(hash, key, value, i);
//        return null;
//    }



    public static void main(String[] args) {
        //Integer 两种构造方法 - hashcode相同，两个对象为什么不相等 - ==比的是内存中的地址么
        Integer a = new Integer(10);
        Integer b = new Integer("10");
        System.out.println(a.hashCode());   //10
        System.out.println(b.hashCode());   //10
        System.out.println(a.hashCode() == b.hashCode());   //true
        System.out.println(a==b);   //false

    }

}
