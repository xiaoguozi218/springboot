package com.example.java8;

import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by MintQ on 2018/7/6.
 *
 * Stream 接口 重要！！！
 *
 *
 */
public class StreamTest {

    public static void main(String[] args) {

        //创建stream–通过of方法
//        Stream<Integer> integerStream = Stream.of(1, 2, 3, 5);
//        Stream<String> stringStream = Stream.of("taobao");

        //创建stream–通过generator方法  -生成一个无限长度的Stream，其元素的生成是通过给定的Supplier（这个接口可以看成一个对象的工厂，每次调用返回一个给定类型的对象）
//        Stream.generate(new Supplier<Double>() {    //方式一
//            @Override
//            public Double get() {
//                return Math.random();
//            }
//        });
//        Stream.generate(()->Math.random()); //方式二
//        Stream.generate(Math::random);  //方式三

        //创建stream–通过iterate方法
//        Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);

        //通过Collection子类获取Stream
        //下面的例子展示了是如何通过并行Stream来提升性能：
//        int max = 1000000;
//        List<String> values = new ArrayList<>(max);
//        for (int i = 0; i < max; i++) {
//            UUID uuid = UUID.randomUUID();
//            values.add(uuid.toString());
//        }

        //串行排序：
//        long t0 = System.nanoTime();
//        long count1 = values.stream().sorted().count();
//        System.out.println(count1);
//        long t1 = System.nanoTime();
//        long millis1 = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
//        System.out.println(String.format("sequential sort took: %d ms", millis1));

        //并行排序：
//        long t2 = System.nanoTime();
//        long count2 = values.parallelStream().sorted().count();
//        System.out.println(count2);
//        long t3 = System.nanoTime();
//        long millis2 = TimeUnit.NANOSECONDS.toMillis(t3 - t2);
//        System.out.println(String.format("parallel sort took: %d ms", millis2));

        //stream的其他应用：
        // 1、count()、max()、min()方法
        List<Integer> collection = new ArrayList<Integer>();
        collection.add(14);
        collection.add(5);
        collection.add(43);
        collection.add(89);
        collection.add(64);
        collection.add(112);
        collection.add(55);
        collection.add(55);
        collection.add(58);
//        //list长度
//        System.out.println(collection.parallelStream().count());
//
//        //求最大值,返回Option,通过Option.get()获取值
//        System.out.println(collection.parallelStream().max((a,b)->{return a-b;}).get());
//
//        //求最小值,返回Option,通过Option.get()获取值
//        System.out.println(collection.parallelStream().min((a,b)->{return a-b;}).get());

        //2、Filter 过滤方法
//        Long count =collection.stream().filter(num -> num!=null).
//                filter(num -> num.intValue()>50).count();
//        System.out.println(count);

        //3、distinct方法  去除重复
//        collection.stream().distinct().forEach(System.out::println);

        //4、Sort 排序 - 排序是一个中间操作，返回的是排序好后的Stream。如果你不指定一个自定义的Comparator则会使用默认排序。

        //5、Map 映射-对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。这个方法有三个对于原始类型的变种方法，
        //          分别是：mapToInt，mapToLong和mapToDouble。这三个方法也比较好理解，比如mapToInt就是把原始Stream转换成一个新的Stream，这个新生成的Stream中的元素都是int类型。之所以会有这样三个变种方法，可以免除自动装箱/拆箱的额外消耗；
        //将String转化为Integer类型
//        collection.stream().mapToInt(Integer::valueOf).forEach(System.out::println);
        //也可以这样用：
//        List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);
//        System.out.println("sum is:"+nums.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2).
//                peek(System.out::println).skip(2).limit(4).sum());

        //7、limit：-对一个Stream进行截断操作，获取其前N个元素，如果原Stream中包含的元素个数小于N，那就获取其所有的元素；
        //8、skip：-返回一个丢弃原Stream的前N个元素后剩下元素组成的新Stream，如果原Stream中包含的元素个数小于N，那么返回空Stream；
        //9、Match 匹配 - Stream提供了多种匹配操作，允许检测指定的Predicate是否匹配整个Stream。所有的匹配操作都是最终操作，并返回一个boolean类型的值。
        //10、Count 计数 - 计数是一个最终操作，返回Stream中元素的个数，返回值类型是long。
        //11、Reduce 规约 - 这是一个最终操作，允许通过指定的函数来讲stream中的多个元素规约为一个元素，规越后的结果是通过Optional接口表示的：

    }

    /**
     * @param <E>
     * 通过Collection子类获取Stream
     *  Java 8扩展了集合类，可以通过 Collection.stream() 或者 Collection.parallelStream() 来创建一个Stream。
     *  Stream有串行和并行两种，串行Stream上的操作是在一个线程中依次完成，而并行Stream则是在多个线程上同时执行。
     */
    public interface Collection<E> extends Iterable<E> {
        //其他方法省略
        default Stream<E> stream() {
            return StreamSupport.stream(spliterator(), false);
        }
    }

}
