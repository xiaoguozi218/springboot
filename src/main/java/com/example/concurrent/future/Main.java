package com.example.concurrent.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by MintQ on 2018/5/9.
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //构造FutrueTask
        FutureTask<String> future = new FutureTask<String>(new RealDataTest("a"));
        ExecutorService executor = Executors.newFixedThreadPool(1);
        //执行FutureTask,相当于上例中的client.request("a)发送请求
        //在这里开启线程进行RealDataTest的call()执行
        executor.submit(future);
        System.out.println("请求完毕");

        try {
            //这里依然可以做额外的数据操作，这里使用sleep代替其他业务逻辑的处理
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //相当于上例中的data.getResult() ， 取得call()方法的返回值
        //如果此时call()方法没有执行完成，则依然会等待
        System.out.print("数据 = " + future.get());
    }

}
