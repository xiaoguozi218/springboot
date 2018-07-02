package com.example.concurrent.thread.communication.tongbu;

import com.example.concurrent.thread.communication.tongbu.MyObject;

/**
 * Created by MintQ on 2018/7/2.
 */
public class ThreadB extends Thread{
    private MyObject object;

    public ThreadB(MyObject object) {
        this.object=object;
    }

    @Override
    public void run() {
        super.run();
        object.methodB();
    }
}
