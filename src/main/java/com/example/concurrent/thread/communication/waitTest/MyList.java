package com.example.concurrent.thread.communication.waitTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MintQ on 2018/7/2.
 */
public class MyList {
    private static List<String> list = new ArrayList<String>();

    public static void add() {
        list.add("anyString");
    }

    public static int size() {
        return list.size();
    }
}
