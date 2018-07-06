package com.example.java8;


/**
 * Created by MintQ on 2018/7/4.
 *
 * 从java8开始，我们就可以通过java8中的 StrameAPI 与 Lambda 表达式实现函数式编程，可以让代码变得更加高效简洁。其中我开发中用得比较多的是，Optional，SteameAPI与lambda。
 *
 * 现在带你领略下Java8的新特性：
 *  一、接口的默认方法
 *     ~在接口中新增了 default 方法和 static 方法，这两种方法可以有方法体
 *
 *
 */
public class LambdaUtils {

    public interface LambdaTest {
        abstract void print();
    }

    public interface LambdaTest2 {
        abstract void print(String s);
    }

    public interface DefaultTest {
        static int a = 5;
        default void defaultMethod() {
            System.out.println("DefaultTest default 方法");
        }

        int sub(int a ,int b);

        static void staticMethod() {
            System.out.println("DefaultTest static 方法");
        }
    }


    public static void main(String[] args) {

        //匿名内部类--java8之前的实现方式
        DefalutTest dt = new DefalutTest(){
            @Override
            public int sub(int a, int b) {
                // TODO Auto-generated method stub
                return a-b;
            }
        };

        //lambda表达式--实现方式1
        DefalutTest dt2 =(a,b)->{
            return a-b;
        };
        System.out.println(dt2.sub(2, 1));

        //lambda表达式--实现方式2，省略花括号
        DefalutTest dt3 =(a,b)->a-b;
        System.out.println(dt3.sub(5, 6));

        //测试final   内部类中引用局部变量，后期试图修改此变量，内部类报错。
        int c = 5;
        DefalutTest dt4 =(a,b)->a-c;
        System.out.println(dt4.sub(5, 6));
//        c = 8;

        //无参方法，并且执行语句只有1条
        LambdaTest lt = ()-> System.out.println("测试无参");
        lt.print();

        //只有一个参数方法
        LambdaTest2 lt1 = s-> System.out.println(s);
        lt1.print("有一个参数");
    }


}
