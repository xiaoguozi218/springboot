package com.example.test.jvm;

import java.util.ArrayList;

/**
 * @Auther: gsh
 * @Date: 2018/8/9 17:10
 * @Description: 内存溢出
 *
 * 一、背景 - 有时程序在运行一段时间后，由于程序的bug，会出现OutOfMemoryError异常。通常情况下这种想象都是由于内存溢出或者大对象造成的内存不够用所导致的。
 *           如何定位到内存溢出的代码或者大对象，便成为了解决问题的关键。
 *
 */
public class TestOOM {

    static class Obj {
        public byte[] bytes = "hello everyone".getBytes();
    }

    public static void main(String[] args) {
        ArrayList<Obj> list = new ArrayList<Obj>();
//        while (true) {
//            list.add(new Obj());
//        }
    }
}
