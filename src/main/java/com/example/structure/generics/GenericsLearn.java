package com.example.structure.generics;

import com.example.pattern.factory.Dog;
import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * @Auther: gsh
 * @Date: 2018/8/15 10:04
 * @Description: 泛型学习
 *
 * 1、什么是泛型？
 *      Java 泛型（generics）是 JDK 5 中引入的一个新特性, 泛型提供了编译期类型安全检测机制，该机制允许程序员在编译时检测到非法的类型。
 *    泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。
 * 2、泛型的声明
 *    1）泛型可以声明在方法中：(泛型方法)
 *    2）泛型可以声明在类中：（泛型类）
 *    3）泛型可以声明在接口中：（泛型接口）
 *
 */
public class GenericsLearn {

    class Dog{

    }

    class Cat{

    }

    public static void main(String[] args) {
        GenericsLearn genericsLearn = new GenericsLearn();
        HashMap<String, Cat> map1 = Maps.newHashMap();
        HashMap<String, Dog> map2 = Maps.newHashMap();
//        map.put("dog",genericsLearn.new Dog());   //不再允许添加
//        map.put("cat",genericsLearn.new Cat());

//        Cat cat =  map.get("cat");
//        System.out.println(cat);


        System.out.println(map1.getClass());
        System.out.println(map2.getClass());
    }


}
