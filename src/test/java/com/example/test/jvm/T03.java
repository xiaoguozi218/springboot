package com.example.test.jvm;

/**
 * Created by MintQ on 2018/6/23.
 * 也可以通过 Runtime类“大致”计算内存情况
 */
public class T03 {

    public static void main(String[] args) {
        printMemoryInfo();
        byte[] b = new byte[1024*1024];
        System.err.println("------");
        printMemoryInfo();
    }

    static void printMemoryInfo() {
        System.err.println("total:"+Runtime.getRuntime().totalMemory());
        System.err.println("free:"+Runtime.getRuntime().freeMemory());
    }
}
