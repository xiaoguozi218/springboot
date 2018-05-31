package com.example.mq.delayqueue;

import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * 延时队列管理类，用来添加任务、执行任务
 * Created by MintQ on 2018/5/30.
 */
public class DelayWithdrawQueueManager {


//    private final static int DEFAULT_THREAD_NUM = 5;
    private final static int thread_num = 4;
    // 固定大小线程池
    private ExecutorService executor;
    // 守护线程
    private Thread daemonThread;
    // 延时队列
    private DelayQueue<DelayWithdrawTask<?>> delayQueue;
    private static final AtomicLong atomic = new AtomicLong(0);
//    private final long n = 1;
    private static DelayWithdrawQueueManager instance = new DelayWithdrawQueueManager();

    private DelayWithdrawQueueManager() {
        executor = Executors.newFixedThreadPool(thread_num);
        delayQueue = new DelayQueue<>();
        init();
    }

    public static DelayWithdrawQueueManager getInstance() {
        return instance;
    }

    /**
     * 初始化
     */
    public void init() {
        daemonThread = new Thread(() -> {
            execute();
        });
        daemonThread.setName("DelayQueueMonitor");
        daemonThread.start();
    }

    private void execute() {
        while (true) {
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            System.out.println("当前存活线程数量:" + map.size());
            int taskNum = delayQueue.size();
            System.out.println("当前延时任务数量:" + taskNum);
            try {
                // 从延时队列中获取任务
                DelayWithdrawTask<?> delayWithdrawTask = delayQueue.take();
                if (delayWithdrawTask != null) {
                    Runnable task = delayWithdrawTask.getTask();
                    if (null == task) {
                        continue;
                    }
                    // 提交到线程池执行task
                    executor.execute(task);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加任务
     *
     * @param task
     * @param time
     *            延时时间
     * @param unit
     *            时间单位
     */
    public void put(Runnable task, long time, TimeUnit unit) {
        // 获取延时时间
        long timeout = TimeUnit.NANOSECONDS.convert(time, unit);
        // 将任务封装成实现Delayed接口的消息体
        DelayWithdrawTask<?> delayOrder = new DelayWithdrawTask<>(timeout, task);
        // 将消息体放到延时队列中
        delayQueue.put(delayOrder);
    }

    /**
     * 删除任务
     *
     * @param task
     * @return
     */
    public boolean removeTask(DelayWithdrawTask task) {

        return delayQueue.remove(task);
    }

}
