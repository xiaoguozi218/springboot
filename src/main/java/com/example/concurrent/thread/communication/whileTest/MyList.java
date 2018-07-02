package com.example.concurrent.thread.communication.whileTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MintQ on 2018/7/2.
 */
public class MyList {
    private List<String> list = new ArrayList<String>();
    public void add() {
        list.add("elements");
    }
    public int size() {
        return list.size();
    }
}
