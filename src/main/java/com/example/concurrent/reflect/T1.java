package com.example.concurrent.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author  gsh
 * @date  2018/10/22 下午8:34
 *
 * 1、获取类：3种方法
 * 2、获取所有的方法：getMethods( )
 * 3、获取所有实现的接口：getInterfaces()
 * 4、获取父类：getSuperclass()
 * 5、获取所有的构造函数：getConstructors()
 * 6、获取所有的属性：getDeclaredFields();
 * 7、创建实例：newInstance()
 * 8、getDeclaredFields 和 getFields 的区别：
 *    - getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
 *    - getFields()获得某个类的所有的公共（public）的字段，包括父类。
 * 9、
 **/
public class T1 {
    public static void main(String[] args) {
        //第一种方法：Class.forName
        try {
            Class<?> class1 = Class.forName("com.example.concurrent.reflect.Person");
            //获取所有的公共的方法
//            Method[] methods =  class1.getMethods() ;
//
//            for (Method method : methods) {
//                System.out.println( method );
//            }

            //获取所有的接口
//            Class<?>[] interS = class1.getInterfaces() ;
//
//            for (Class<?> class2 : interS ) {
//                System.out.println( class2 );
//            }

            //获取父类
//            Class<?> superclass = class1.getSuperclass() ;
//            System.out.println( superclass );

            //获取所有的构造函数
//            Constructor<?>[] constructors = class1.getConstructors() ;
//
//            for (Constructor<?> constructor : constructors) {
//                System.out.println( constructor );
//            }

            //取得本类的全部属性
//            Field[] field = class1.getDeclaredFields();
//
//            for (Field field2 : field) {
//                System.out.println( field2 );
//            }

            //创建实例化：相当于 new 了一个对象
//            Object object = class1.newInstance() ;
//            //向下转型
//            Person person = (Person) object ;




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //第二种方法：类名.class
//        Class<?> class2 = Person.class;
//        //第三种方法：实例.getClass
//        Person person = new Person();
//        Class<?> class3 = person.getClass();
//
//        System.out.println( class2 );
//        System.out.println( class3 );


    }
}
