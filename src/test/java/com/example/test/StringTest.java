
package com.example.test;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    static class Parent{
        public Parent() {
            System.out.println("Parent");
        }
        {
            System.out.println("Im Parent class");
        }
        static {
            System.out.println("static Parent");
        }
    }

    static class Child extends Parent{
        public Child() {
            System.out.println("Child");
        }
        {
            System.out.println("Im Child class");
        }
        static {
            System.out.println("static Child");
        }
    }

    static void pong() {
        System.out.print("pong");
    }

    private static void modify(String input) {
        input = input + "modified.";
    }
}
