
package com.example.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by gsh on 2018/5/17.
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
 *《Java字符串池（String Pool）深度解析》- 字符串常量池则存在于方法区
 *  1、背景 - 在工作中，String类是我们使用频率非常高的一种对象类型。
 *     JVM为了提升性能和减少内存开销,避免字符串的重复创建,其维护了一块特殊的内存空间,这就是我们今天要讨论的核心,即字符串池(String Pool).字符串池由String类私有的维护。
 *  2、在Java中有两种创建字符串对象的方式：- 这两种方式在性能和内存占用方面存在着差别。
 *      1）采用字面值的方式赋值
 *      2）采用new关键字新建一个字符串对象。
 *  3、字符串池的优缺点：
 *     - 优点：字符串池的优点就是避免了相同内容的字符串的创建，节省了内存，省去了创建相同字符串的时间，同时提升了性能；
 *     - 缺点：缺点就是牺牲了JVM在常量池中遍历对象所需要的时间，不过其时间成本相比而言比较低。
 *  4、intern方法使用：一个初始为空的字符串池，它由类String独自维护。当调用 intern方法时，如果池已经包含一个等于此String对象的字符串（用equals(oject)方法确定），
 *      则返回池中的字符串。否则，将此String对象添加到池中，并返回此String对象的引用。
 *      对于任意两个字符串s和t，当且仅当s.equals(t)为true时，s.instan() == t.instan才为true。所有字面值字符串和字符串赋值常量表达式都使用 intern方法进行操作。
 *  5、Java语言规范中对字符串做出了如下说明：- 用更通俗易懂的语言翻译过来主要说明了三点：
 *      1）每一个字符串常量都指向字符串池中或者堆内存中的一个字符串实例；
 *      2）字符串对象值是固定的，一旦创建就不能再修改；
 *      3）字符串常量或者常量表达式中的字符串都被使用方法String.intern()在字符串池中保留了唯一的实例。
 *  6、总结来说就是：字面量"+"拼接是在 编译期间 进行的，拼接后的字符串存放在字符串池中；而字符串引用的"+"拼接运算实在 运行时 进行的，新创建的字符串存放在堆中。
 *  7、总结：1）字符串是常量，字符串池中的每个字符串对象只有唯一的一份，可以被多个引用所指向，避免了重复创建内容相同的字符串；
 *          2）通过字面值赋值创建的字符串对象存放在字符串池中，通过关键字new出来的字符串对象存放在堆中。
 *
 *《面试题》：
 *  1、String str4 = new String(“abc”) 创建多少个对象？- 所以，常量池中没有“abc”字面量则创建两个对象，否则创建一个对象。
 *     1）在常量池中查找是否有“abc”对象
 *        - 有则返回对应的引用实例
 *        - 没有则创建对应的实例对象
 *     2）在堆中 new 一个 String("abc") 对象
 *     3）将对象地址赋值给str4,创建一个引用
 *  2、String str1 = new String("A"+"B") ; 会创建多少个对象? - 总共 ： 4个
 *     - str1：字符串常量池："A","B","AB" : 3个
 *             堆：new String("AB") ：1个
 *
 *  3、String str2 = new String("ABC") + "ABC" ; 会创建多少个对象? - 总共 ： 2个
 *     - str2 ：字符串常量池："ABC" : 1个
 *              堆：new String("ABC") ：1个
 *
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
