package com.example.mq.delayqueue;

/**
 * 下面来看看具体代码实现：

 在项目中有如下几个类：第一 、任务类   第二、按照任务类组装的消息体类  第三、延迟队列管理类

 任务类即执行筛选司机、绑单、push消息的任务类
 *
 *
 *
 *  具体执行相关业务的业务类
 * Created by MintQ on 2018/5/30.
 */
public class DelayWithdrawWorker implements Runnable{




    @Override
    public void run() {
        //TODO
        //相关业务逻辑处理
        System.out.println(Thread.currentThread().getName()+" do something ……");
    }
}
