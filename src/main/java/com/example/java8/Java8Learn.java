package com.example.java8;

import com.example.model.Person;
import lombok.Data;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MintQ on 2018/7/6.
 *
 * 从java8开始，我们就可以通过java8中的 StrameAPI 与 Lambda 表达式实现函数式编程，可以让代码变得更加高效简洁。其中我开发中用得比较多的是，Optional，SteameAPI与lambda。
 *
 *
 *
 * 现在带你领略下Java8的新特性：
 *  一、接口的默认方法
 *     ~在接口中新增了 default 方法和 static 方法，这两种方法可以有方法体
 *  二、Lambda 表达式
 *      ~Lambda表达式可以看成是匿名内部类，使用Lambda表达式时，接口必须是函数式接口
 *          基本语法：   <函数式接口>  <变量名> = (参数1，参数2...) -> {
                                        //方法体
                    }
 *          说明： (参数1，参数2…)表示参数列表；->表示连接符；{}内部是方法体
 *              1、=右边的类型会根据左边的函数式接口类型自动推断；
 *              2、如果形参列表为空，只需保留()；
 *              3、如果形参只有1个，()可以省略，只需要参数的名称即可；
 *              4、如果执行语句只有1句，且无返回值，{}可以省略，若有返回值，则若想省去{}，则必须同时省略return，且执行语句也保证只有1句；
 *              5、形参列表的数据类型会自动推断；
 *              6、lambda不会生成一个单独的内部类文件；
 *              7、lambda表达式若访问了局部变量，则局部变量必须是final的，若是局部变量没有加final关键字，系统会自动添加，此后在修改该局部变量，会报错；
 *
 *
 *
 *  八、访问接口的默认方法
 *      ~Predicate接口
 *      ~Function 接口
 *      ~Supplier 接口
 *      ~Consumer 接口
 *      ~Comparator 接口
 *      ~Optional 接口
 *      ~Stream 接口 重要！！！
 *      ~
 *
 *  九、Date API
 *
 *《Java8解决了什么？》：下面我就来探索一下，Java8到底解决了一些什么问题。
 *  - 其实很简单，我们对工具的改造的最终目的都是为了解决问题，以前有面向过程解决不了的问题，那么面向对象出来解决了；现在面向对象有许多问题，那么就可以用函数式编程来解决，所以这些变化是很自然的，Java要在不同时代的保持自己的活力，就必须与时俱进，所以Java8的出现就是自然而然的。
 *  1、消除冗余类代码 - 函数式的lambda表达式通过将函数提升为“一等公民”，使得直接传递函数成为可能，而不必再为了传递实现某个功能的函数而强行传递一个冗余的外包类。
 *  2、内部迭代替代外部迭代 -
 *  3、安全简洁地并行 - 如果我们有大量数据要处理，通常会使用多线程，在Java8之前，使用多线程是一件比较麻烦的事：
 *      - 我们得自己合理的划分数据
 *      - 手动为每一部分数据单独分配一个线程，还有可能会产生竞态条件需要进行同步
 *      - 完成每个线程的结果的合并，得到最终结果。
 *      这个过程是比较麻烦的，易错的。使用流能够安全简洁的使用多核，甚至于你都不需要关心多线程的具体实现。
 *
 */
public class Java8Learn {


    public static void main(String[] args) {
        List<Person> workers = new LinkedList<>();
        workers.add( new Person("aa",23));
        workers.add( new Person("abc",21));
        workers.add( new Person("cdf",18));

        //1、如果要对这个list按照People的年龄排序，并打印出来，那么在Java8之前会这样写：
        workers.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge()>o2.getAge()?1:-1;
            }
        });
        for (Person p:workers ) {
            System.out.println(p.getName()+":"+p.getAge());
        }

        //Java8引入了函数式编程的lambda表达式，就可以这样写了：
        workers.sort((o1, o2) -> o1.getAge()>o2.getAge()?1:-1);
        for (Person p:workers ) {
            System.out.println(p.getName()+":"+p.getAge());
        }

        //进一步，使用方法引用就可以这样写：
        workers.sort(Comparator.comparing(Person::getAge));
        for (Person p:workers ) {
            System.out.println(p.getName()+":"+p.getAge());
        }

        //2、stream允许你以声明性方式处理数据集合，类似于SQL语句 -
        // 这一段代码根本就没有循环了，Stream API 替你搞定了循环，这就是内部迭代 替代 外部迭代 ，亦即API的设计者 替 使用者完成了迭代，代码相当简洁。
        //而且由于是内部迭代，所以Stream库可以选择最适合本机硬件的实现，达到性能优化的目的，如果是外部迭代，就需要调用者自己来优化了（你得承认许多API调用者没有这种优化能力）。
        workers.sort(Comparator.comparing(Person::getAge));
        workers.stream().map(p-> p.getName()+":"+p.getAge()).forEach(System.out::println);


    }


}
