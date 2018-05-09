package com.example.concurrent.future;

import java.util.concurrent.Callable;

/**
 * Created by MintQ on 2018/5/9.
 */
public class RealDataTest implements Callable<String> {

    private String para;

    public RealDataTest(String para) {
        this.para = para;
    }

    @Override

    public String call() throws Exception {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 10 ; i++) {
            sb.append(para);
            Thread.sleep(100);

        }

        return sb.toString();
    }
}
