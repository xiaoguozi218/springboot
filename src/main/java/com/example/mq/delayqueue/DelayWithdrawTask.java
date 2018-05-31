package com.example.mq.delayqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列中的消息体将任务封装为消息体
 * Created by MintQ on 2018/5/30.
 */
public class DelayWithdrawTask<T extends Runnable> implements Delayed {

    private final long time;
    private final T task; // 任务类，也就是之前定义的任务类

    public DelayWithdrawTask(long timeout, T task) {
        this.time = System.nanoTime() + timeout;
        this.task = task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // TODO Auto-generated method stub
        return unit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        // TODO Auto-generated method stub
        DelayWithdrawTask other = (DelayWithdrawTask) o;
        long diff = time - other.time;
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }

    public T getTask() {
        return task;
    }
}
