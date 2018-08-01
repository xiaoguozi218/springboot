package com.example.concurrent.thread.threadlocal;

import java.util.Random;

/**
 * @Auther: MintQ
 * @Date: 2018/8/1 15:07
 * @Description: 切底明白ThreadLocal的原理与使用
 *
 *
 */
public class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws InterruptedException {
        final Person p = new Person();
        //这样做其实就是在操作同一个对象，如果需要实现多线程应该像下下面的注释一样，这样就针对于每一个线程创建一个独立的Person对象
        final ThreadLocal<Person> t = new ThreadLocal<Person>(){
            public Person initialValue() {
                //return new Person();
                return p;
            }
        };

        p.setName("小明");
        for(int i=0;i<3;i++)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    t.set(p);
                    t.get().setName(new Random().nextInt(100)+"");
                    System.out.println(t.get().getName()+"=="+t.get());
                }
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(p.getName());
        //这里给出运行的答案，其实这个程序每一次运行的结果都是不一样的。为什么会这样的？
        //我们知道，ThreadLocal是数据隔离，如果按照以前的理解，应该是最后的输出一定是不会改变的，也就是“小明”；其实，这里不一样，是因为每一个线程保存的Person对象的地址都是一样的，这里的运行结果可以看出。
    }
}
