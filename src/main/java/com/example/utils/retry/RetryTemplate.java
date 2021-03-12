package com.example.utils.retry;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * 模板方式 实现重试
 * https://www.cnblogs.com/panchanggui/p/11232915.html
 * 优点：
 *  简单（依赖简单：引入一个类就可以了； 使用简单：实现抽象类，讲业务逻辑填充即可；）
 *  灵活（这个是真正的灵活了，你想怎么干都可以，完全由你控制）
 *
 * 缺点：
 *  强侵入
 *  代码臃肿
 *
 * @author  gsh
 * @date  2019/12/19 下午3:04
 **/
@Slf4j
public abstract class RetryTemplate {
    private static final int DEFAULT_RETRY_TIME = 3;

    private int retryTime = DEFAULT_RETRY_TIME;

    // 重试的睡眠时间
    private int sleepTime = 100;

    public int getSleepTime() {
        return sleepTime;
    }

    public RetryTemplate setSleepTime(int sleepTime) {
        if(sleepTime < 0) {
            throw new IllegalArgumentException("sleepTime should equal or bigger than 0");
        }

        this.sleepTime = sleepTime;
        return this;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public RetryTemplate setRetryTime(int retryTime) {
        if (retryTime <= 0) {
            throw new IllegalArgumentException("retryTime should bigger than 0");
        }

        this.retryTime = retryTime;
        return this;
    }

    /**
     * 重试的业务执行代码
     * 失败时请抛出一个异常
     * todo 确定返回的封装类，根据返回结果的状态来判定是否需要重试
     * @return
     */
    protected abstract <T> T doBiz() throws Exception;


    /**
     *
     * @author  gsh
     * @date  2020/1/1 下午10:01
     * @Param bizType 业务类型
     * @return Object
     * @throws InterruptedException
     **/
    public <T> T execute(String bizType) throws InterruptedException {
        for (int i = 0; i < retryTime; i++) {
            try {
                return doBiz();
            } catch (Exception e) {
                log.error("{},业务执行出现异常,e:", bizType,e);
                Thread.sleep(sleepTime);
            }
        }
        return null;
    }


//    public Object submit(ExecutorService executorService) {
//        if (executorService == null) {
//            throw new IllegalArgumentException("please choose executorService!");
//        }
//
//        return executorService.submit((Callable) () -> execute(""));
//    }

//    /**
//     * retryDemo
//     * @author  gsh
//     * @date  2019/12/19 下午3:09
//     **/
//    public static void retryDemo() throws InterruptedException {
//        Object ans = new RetryTemplate() {
//            @Override
//            protected Object doBiz() throws Exception {
////                int temp = (int) (Math.random() * 10);
//                int temp = 4;
//                System.out.println(temp);
//
//                if (temp > 3) {
//                    throw new Exception("generate value bigger then 3! need retry");
//                }
//
//                return temp;
//            }
//        }.setRetryTime(4).setSleepTime(10).execute("");
//        System.out.println(ans);
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        retryDemo();
//    }

}
