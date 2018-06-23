package com.example.test.jvm;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by MintQ on 2018/6/23.
 *
 * 内存溢出
 * -XX:+
 * -Xms10M -Xmx10M
 * 内存文件的查看工具VisualVM
 *
 */
public class T04 {

    public static void main(String[] args) {
        List<Object> lists = new ArrayList<>();
        for (int i = 0; i < 1000000 ; i++) {
            lists.add(new byte[1024*1024]);
        }
    }
}
