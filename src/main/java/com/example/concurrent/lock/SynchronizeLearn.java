package com.example.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/** 一键格式化代碼： Ctrl+Alt+L
 * @author  gsh
 * @date  2019/11/20 上午9:54
 * 1、阿里面试题，为什么wait()方法要放在同步块中？ 关键词：Lost Wake-Up Problem
 *  假如面试官问你这个问题了，你最开始不要巴啦啦全部说出来。只需要轻描淡写地说：“这是Java设计者为了避免使用者出现lost wake up问题而搞出来的。”
 *
 *
 *
 **/
public class SynchronizeLearn {

    public static void main(String[] args) {
        //
        try {
            new Object().wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
