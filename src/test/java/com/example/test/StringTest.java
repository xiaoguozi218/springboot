
package com.example.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by MintQ on 2018/5/17.
 *
 * 为什么String要设计成不可变的?
 *  1. 字符串常量池的需要：
 *      ~字符串常量池 是Java堆内存中一个特殊的存储区域, 当创建一个String对象时,假如此字符串值已经存在于常量池中,则不会创建一个新的对象,而是引用已经存在的对象。
 *  2. 允许String对象缓存HashCode
 *      ~Java中String对象的哈希码被频繁地使用, 比如在hashMap 等容器中。
 *      ~字符串不变性保证了hash码的唯一性,因此可以放心地进行缓存.这也是一种性能优化手段,意味着不必每次都去计算新的哈希码.
 *  3. 安全性
 *      ~String被许多的Java类(库)用来当做参数,例如 网络连接地址URL,文件路径path,还有反射机制所需要的String参数等, 假若String不是固定不变的,将会引起各种安全隐患。
 *
 */
public class StringTest {




    public static void main(String[] args) {

//        String s1 = "123";
//
//        String s2 = "123";
//
//        String news3 = new String("123");
//
//        System.out.println(s1==s2);
//
//        System.out.println(s1==news3);
//
//        System.out.println(s1);
//
//        System.out.println(s2);
//
//        System.out.println(news3.intern());

//        Set set = new TreeSet();
//
//        set.add("2");
//        set.add(3);
//        set.add("1");
//
//        Iterator it = set.iterator();
//            while (it.hasNext())
//        System.out.print(it.next()+"");

//        class Foo{
//            public int i =3;
//        }
//        Object o = (Object) new Foo();
//        Foo foo = (Foo) o;
//        System.out.print(foo.i);

//        String str = "hello";
//        String str2 = "hello";
//        String str3 = "he"+"llo";
//        String str4 = "he"+new String("llo");
//        System.out.println(str==str2);
//        System.out.println(str==str3);
//        System.out.println(str==str4);

//        Thread t = new Thread(){
//            public void run() {
//                pong();
//            }
//        };
//        t.run();
//        System.out.print("ping");

//        new Child();

//        try {
//            //TODO
//            new IOException();
//        } catch (FileNotFoundException ex){
//            System.out.println("File");
//        } catch (IOException ex){
//            System.out.println("IOE");
//        } catch (Exception ex){
//            System.out.println("exception!");
//        }

//        System.out.println(getValue(2));
        /**
         * 我们再来解释String在传递过程中的步骤：
         *  1   虚拟机在堆中开辟一块内存，并存值”ab”。
            2   虚拟机在栈中分配给str一个内存，内存中存的是1中的地址。
            3   虚拟机复制一份str，我们叫str1，str和str1内存不同，但存的值都是1的。
            4   将str1传入方法体
            5   方法体在堆中开辟一块内存，并存值”cd”
            6   方法体将str1的值改变，存入5的内存地址
            7   方法结束，方法外打印str，由于str存的是1的地址，所有打印结果是”ab”
         *
         */
        String str = "ab";
        modify(str);
        System.err.println(str);
//        System.err.println(modify);


    }


    private static String modify(String input) {
        return input = "cd";
    }

//    public static int getValue(int i) {
//        int result = 0;
//        switch (i) {
//            case 1:
//                result = result+i;
//                break;
//            case 2:
//                result = result+i*2;
//            case 3:
//                result=result+i*3;
//        }
//        return result;
//    }

//    interface Playable{
//        void play();
//    }
//
//    interface Rollable extends Playable{
//        Ball ball = new Ball("Ball");
//    }
//
//    class Ball implements Rollable{
//
//        private String name;
//
//        public String getName() {
//            return name;
//        }
//
//        public Ball(String name) {
//            this.name = name;
//        }
//
//        public final void play() {
//            ball = this;
//            System.out.println(ball.getName());
//        }
//    }


    static void pong() {
        System.out.print("pong");
    }


}
