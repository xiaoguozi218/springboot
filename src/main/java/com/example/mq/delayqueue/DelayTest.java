package com.example.mq.delayqueue;

import java.util.concurrent.TimeUnit;

/**
 * Created by MintQ on 2018/5/30.
 */
public class DelayTest {

    public static void main(String[] args) {
        DelayWithdrawWorker work1 = new DelayWithdrawWorker();// 任务1
        DelayWithdrawWorker work2 = new DelayWithdrawWorker();// 任务2
        DelayWithdrawWorker work3 = new DelayWithdrawWorker();// 任务3
        // 延迟队列管理类，将任务转化消息体并将消息体放入延迟对列中等待执行
        DelayWithdrawQueueManager manager = DelayWithdrawQueueManager.getInstance();

        for (int i = 0; i < 10 ; i++) {
            work1.setExecuteNum(work1.getExecuteNum()+1);
            if (work1.getExecuteNum() <=3 ) {
                System.out.println(work1.getExecuteNum());
                manager.put(work1, 3000, TimeUnit.MILLISECONDS);
            }
        }

//        manager.put(work1, 3000, TimeUnit.MILLISECONDS);
//        manager.put(work2, 6000, TimeUnit.MILLISECONDS);
//        manager.put(work3, 9000, TimeUnit.MILLISECONDS);
    }

}
