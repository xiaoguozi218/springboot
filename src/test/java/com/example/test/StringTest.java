
package com.example.test;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by MintQ on 2018/5/17.
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

        class Foo{
            public int i =3;
        }
        Object o = (Object) new Foo();
        Foo foo = (Foo) o;
        System.out.print(foo.i);


    }
}
