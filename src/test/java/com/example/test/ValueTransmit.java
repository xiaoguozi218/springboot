package com.example.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MintQ on 2018/7/2.
 *
 * Java : 关于值传递你需要了解的事情
 *  我们都知道，在Java中，方法的参数传递永远都是指 值传递。让我们来看一看 基本类型 和 集合 的参数传递在内存中是如何体现的。
 *  在讨论Java中参数是如何传递之前，我们有必要先弄清楚Java的变量（主要指的是基本类型和对象）是怎么存储在内存中的。
 *      基本类型一般都存储在栈中；
 *      对于Java对象，实际的对象数据存储在堆中，而对象的指针（指向堆中的对象引用）存储在栈中。
 *  1.传值 vs 传引用
 *      ~传值：当方法参数是值传递时，意味着原参数的一个 拷贝 被传到了参数内部而不是原始参数，所以任何对于该参数的改变都只会影响这个拷贝值。
 *      ~传引用：当方法参数是引用传递时，意味着原始参数的 引用或者说指针 被传递到了方法内部，而不是这个原始参数的内容。
 *  2.在Java中参数是怎么传递的
 *    在Java中，不管原始参数的类型是什么，参数都是按 值传递 的。每次当一个方法被执行的时候，在栈中就会为每个参数创建一个拷贝，这个拷贝会被传递到方法内部。
 *      ~如果原始参数是基本类型，那么在栈中创建的便是这个参数的简单拷贝
 *      ~如果原始参数不是基本类型，那么在栈中创建的便是指向真正对象数据的新的引用或指针。这个新的引用被传递到方法内部（在这种情况下，有2个引用指向了同一个对象数据）
 *
 *  结论：
 *      在Java中，参数都是按 值传递 的。被传递到方法中的拷贝值，要不就是一个引用或一个变量，取决于原始参数的类型。从现在开始，下面的几条规则将帮助你理解方法中对于参数的修改怎么影响原始参数变量。
 *          ~在方法中，修改一个基础类型的参数永远不会影响原始参数值。
 *          ~在方法中，改变一个对象参数的引用永远不会影响到原始引用。然而，它会在堆中创建了一个全新的对象。（译者注：指的是包装类和immutable对象）
 *          ~在方法中，修改一个对象的属性会影响原始对象参数。
 *          ~在方法中，修改集合和Maps会影响原始集合参数。
 *
 *      因此可见：在Java中所有的参数传递，不管基本类型还是引用类型，都是值传递，或者说是副本传递。
 *
 */
public class ValueTransmit {

    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        System.out.print("Values of x & y 修改之前的值: ");
        System.out.println(" x = " + x + " ; y = " + y );
        modifyPrimitiveTypes(x,y);
        System.out.print("Values of x & y 修改之后的值: ");
        System.out.println(" x = " + x + " ; y = " + y );
        /**
         * 说明：基本类型参数
         * x,y这2个参数是基本类型，所以存储在栈中。当调用modifyPrimitiveTypes()方法时，在堆栈中创建了这2个参数的拷贝（我们就叫它们w,z）,
         * 实际上是w,z被传递到了方法中。所以原始的参数并没有被传递到方法中，在方法中的任何修改都只作用于参数的拷贝w,z
         */

        Integer obj1 = new Integer(1);
        Integer obj2 = new Integer(2);
        System.out.print("Values of obj1 & obj2 修改之前的值: ");
        System.out.println("obj1 = " + obj1.intValue() + " ; obj2 = " + obj2.intValue());
        modifyWrappers(obj1, obj2);
        System.out.print("Values of obj1 & obj2 修改之后的值: ");
        System.out.println("obj1 = " + obj1.intValue() + " ; obj2 = " + obj2.intValue());
        /**
         * 说明：包装类
             包装类存储在堆中，在栈中有一个指向它的引用
             当调用modifyWrappers()方法时，在栈中为每个引用创建了一个拷贝，这些拷贝被传递到了方法里。任何在方法里面的修改都只是改变了引用的拷贝，而不是原始的引用
         *  P.S: 如果方法中的表达式为x += 2，x值得改变也不会影响到方法外部，因为包装类是immutable类型的。当他们的state变化时，他们就会创建一个新的实例。如果你想了解更多关于immutable类，可以阅读How to create an immutable class in Java。字符串类型和包装类相似，所以以上的规则对于字符串也有效。
         */

        List<Integer> lstNums = new ArrayList<Integer>();
        lstNums.add(1);
        System.out.println("Size of list 修改之前的值: = " + lstNums.size());
        modifyList(lstNums);
        System.out.println("Size of list 修改之后的值: = " + lstNums.size());
        /**
         * 说明：
         *   ~当我们创建一个ArrayList或任意集合，在栈中便会创建一个指向堆中多个对象的引用。当modifyList()被调用时，一个引用的拷贝被创建中传递到了方法中。现在有2个引用指向了真正的对象数据，其中任何一个引用的数据改变会影响到另一个。
         *   ~在方法中，当我们调用lstParam.add(2)时，一个Integer对象在堆中被创建，添加到了现有的list对象。所以原始的list引用可以看见这次修改，因为2个引用都指向了内存中的同一个对象
         */

        Student student = new Student();
        System.out.println("Value of name 修改之前的值: = " + student.getName());
        modifyStudent(student);
        System.out.println("Value of name 修改之后的值: = " + student.getName());
        /**
         * 说明：
         *  student对象在堆中被创建，在栈中存储着指向它的引用。当调用calling modifyStudent()，在栈中创建了这个引用的拷贝，传递到了方法中。所以任何对这个对象属性的修改会影响原始的对象引用
         */

    }

    private static void modifyPrimitiveTypes(int x, int y){
        x = 5;
        y = 10;
    }

    private static void modifyWrappers(Integer x, Integer y)
    {
        x = new Integer(5);
        y = new Integer(10);
    }

    private static void modifyList(List<Integer> lstParam)
    {
        lstParam.add(2);
    }

    private static void modifyStudent(Student student)
    {
        student.setName("Alex");
    }

    static class Student{
        private String name;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
