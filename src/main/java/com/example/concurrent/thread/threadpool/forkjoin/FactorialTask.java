package com.example.concurrent.thread.threadpool.forkjoin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * @author  xiaoguozi
 * @create  2018/7/28 下午11:34
 * @desc
 *  我们来实现一个继承 RecursiveTask 的类，计算阶乘，并把任务根据阈值划分成子任务。
 *  这个类需要实现的主要方法就是重写 compute() 方法，用于合并每个子任务的结果。
 *
 **/
public class FactorialTask extends RecursiveTask<BigInteger> {

    private int start = 1;
    private int n;
    private static final int THRESHOLD = 20;

    public FactorialTask(int n) {
        this.n = n;
    }

    public FactorialTask(int start, int n) {
        this.start = start;
        this.n = n;
    }

    @Override
    protected BigInteger compute() {
        if ((n - start) >= THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .reduce(BigInteger.ONE, BigInteger::multiply);
        } else {
            return calculate(start, n);
        }
    }

    /**
     * @author  xiaoguozi
     * @create  2018/7/28 下午11:41
     * @desc    具体划分任务逻辑在 createSubtasks() 方法中：
     **/
    private Collection<FactorialTask> createSubtasks() {
        List<FactorialTask> dividedTasks = new ArrayList<>();
        int mid = (start + n) / 2;
        dividedTasks.add(new FactorialTask(start, mid));
        dividedTasks.add(new FactorialTask(mid + 1, n));
        return dividedTasks;
    }

    //最后，calculate() 方法包含一定范围内的乘数。
    private BigInteger calculate(int start, int n) {
        return IntStream.rangeClosed(start, n)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }

    public static void main(String[] args) {
        //接下来，任务可以添加到线程池：
        ForkJoinPool pool = ForkJoinPool.commonPool();
        BigInteger result = pool.invoke(new FactorialTask(100));
        System.err.println(result);
    }
}
