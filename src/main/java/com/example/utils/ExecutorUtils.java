package com.example.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池工具类
 */
@Configuration
@PropertySource(value = "classpath:threadpool.properties")
public class ExecutorUtils {

    private volatile ExecutorService callbackExecutorService;

    private volatile ExecutorService httpExecutorService;

    @Value("${thread.callback.count}")
    private int callbackCount;
    @Value("${thread.http.count}")
    private int httpCount;
    

    public int getCallbackCount() {
        return callbackCount;
    }

    public void setCallbackCount(int callbackCount) {
        this.callbackCount = callbackCount;
    }

    public int getHttpCount() {
		return httpCount;
	}

	public void setHttpCount(int httpCount) {
		this.httpCount = httpCount;
	}

	public void callbackSubmit(Runnable task){
        if(callbackExecutorService != null){
            callbackExecutorService.submit(task);
            return;
        }
        synchronized (this){
            if(callbackExecutorService == null){
                callbackExecutorService = Executors.newFixedThreadPool(callbackCount);
            }
            callbackExecutorService.submit(task);
        }
    }

    public Future<String> httpSubmit(Callable<String> task){
    	if(httpExecutorService != null){
    		httpExecutorService.submit(task);
    		return null;
    	}
    	synchronized (this){
    		if(httpExecutorService == null){
    			httpExecutorService = Executors.newFixedThreadPool(httpCount);
    		}
    		return httpExecutorService.submit(task);
    	}
    }
}
