package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * Created by gsh on 2018/5/11.
 *
 *
 * 本文主要讲了在Spring Boot项目中的Spring-Retry简单应用，主要是基于注解配置一些重试的策略，使用比较简单。主要的适用场景为在调用第三方接口或者使用mq时。由于会出现网络抖动，连接超时等网络异常，这时就需要重试。
 *
 * @Retryable的参数说明：

value：抛出指定异常才会重试
include：和value一样，默认为空，当exclude也为空时，默认所以异常
exclude：指定不处理的异常
maxAttempts：最大重试次数，默认3次
backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
 *
 */
@Service
public class PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayService.class);

    private final int totalNum = 100000;

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public int minGoodsnum(int num) throws Exception {
        logger.info("减库存开始" + LocalTime.now());
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            logger.error("illegal");
        }
        if (num <= 0) {
            throw new IllegalArgumentException("数量不对");
        }
        logger.info("减库存执行结束" + LocalTime.now());
        return totalNum - num;
    }


    /**
     * @param e
     * @return
     *
     * 当重试耗尽时，RetryOperations可以将控制传递给另一个回调，即RecoveryCallback。Spring-Retry还提供了@Recover注解，用于@Retryable重试失败后处理方法，此方法里的异常一定要是@Retryable方法里抛出的异常，否则不会调用这个方法。
     *
     */
    @Recover
    public int recover(Exception e) {
        logger.warn("减库存失败！！！" + LocalTime.now());
        return totalNum;
    }

}
