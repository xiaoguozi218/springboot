package com.example.java8;

/**
 * Created by MintQ on 2018/7/6.
 *
 * 从java8开始，我们就可以通过java8中的 StrameAPI 与 Lambda 表达式实现函数式编程，可以让代码变得更加高效简洁。其中我开发中用得比较多的是，Optional，SteameAPI与lambda。
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
 *
 *
 */
public class Java8Learn {


}