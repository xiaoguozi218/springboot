package com.example.concurrent;

/**
 * Created by MintQ on 2018/5/9.
 * Future数据，构造很快，但是是一个虚拟的数据，需要装配RealData
 */
public class FutureData implements Data {

    protected RealData realData = null;
    protected boolean isReady = false;

    public synchronized void setRealData(RealData realData) {
        if (isReady) {
            return;
        }
        this.realData = realData;
        isReady = true;
        notifyAll();
    }

    @Override
    public synchronized String getResult() {
        while (!isReady) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return realData.result;
    }
}
