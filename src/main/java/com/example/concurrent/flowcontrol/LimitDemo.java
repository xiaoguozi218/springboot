package com.example.concurrent.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by MintQ on 2018/7/9.
 * <p>
 * 限流神器：Guava RateLimiter
 */
public class LimitDemo {

    public static ConcurrentHashMap<String, RateLimiter> resourceRateLimiter = new ConcurrentHashMap<String, RateLimiter>();

    static {
        createResourceLimiter("order",50);
    }

    public static void createResourceLimiter(String resource,double qps) {
        if (resourceRateLimiter.contains(resource)) {
            resourceRateLimiter.get(resource).setRate(qps);
        } else {
            RateLimiter rateLimiter = RateLimiter.create(qps);
            resourceRateLimiter.putIfAbsent(resource, rateLimiter);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (resourceRateLimiter.get("order").tryAcquire(10, TimeUnit.MILLISECONDS)) {
                        System.out.println("执行业务逻辑");
                    } else {
                        System.out.println("限流");
                    }
                }
            }).start();
        }
    }
}

