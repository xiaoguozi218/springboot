//package com.example.utils.task;
//
//import com.example.service.RedisService;
//import com.example.utils.HttpUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * Created by gsh on 2018/7/19.
// */
//@Component //使spring管理
//public class Timer {
//    private static final Logger logger = LoggerFactory.getLogger(Timer.class); // 日志记录
//
//    @Autowired
//    RedisService redisService;
//
//    @Scheduled(fixedRate = 10000)
//    public void scheduler(){
//        try {
//            if (redisService.get("key") == null) {
//                if(redisService.setScheduler("key", "value")){
//
//                    //定时任务执行代码
//                    Thread.sleep(3000);
//                }
//            }
//        } catch (InterruptedException e) {
//            logger.error("定时任务异常");
//        }finally{
//            redisService.del("key");
//        }
//    }
//
//
//}
