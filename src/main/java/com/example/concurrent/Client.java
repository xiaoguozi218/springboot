package com.example.concurrent;

/**
 * Created by MintQ on 2018/5/9.
 * 返回Data对象，立即返回FutureData,并开启ClientThread线程装配RealData
 */
public class Client {

    public Data request(final String queryStr) {
        final FutureData future = new FutureData();
        //RealData的构造很慢
        new Thread() {
            public void run(){
                RealData realData = new RealData(queryStr);
                future.setRealData(realData);
            }
        }.start();
        return future;
    }

    public static void main(String[] args) {
        Client client = new Client();

        Data data = client.request("a");
        System.out.println("请求完毕");

        try {
            //这里可以用一个sleep代替了对其他业务逻辑的处理
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //使用真实的数据
        System.out.println("数据 = " + data.getResult());
    }
}
