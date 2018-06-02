package com.example.service;

import com.example.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by MintQ on 2018/5/18.
 */
@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private StringRedisTemplate rt;

    /**
     * 当前key是否存在
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return rt.hasKey(key);
    }

    /**
     * 增加key和value并且设置到期时间
     * @param key
     * @param value
     * @param date
     */
    public void set(String key, String value, Date date) {
        rt.opsForValue().set(key, value);
        rt.expireAt(key, date);
    }


    /**
     * 通过key获取对应的String值
     * @param key
     * @return
     */
    public String getString(String key) {
        return rt.opsForValue().get(key);
    }

    /**
     * 增加key和value并且设置过期时间
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void set(String key, String value, Long timeout, TimeUnit unit) {
        rt.opsForValue().set(key, value, timeout, unit);
    }



    public boolean set(final String key, final String value) {
        boolean result = rt.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = rt.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    public String get(final String key){
        String result = rt.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = rt.getStringSerializer();
                byte[] value =  connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }


    public boolean expire(final String key, long expire) {
        return rt.expire(key, expire, TimeUnit.SECONDS);
    }

    public <T> boolean setList(String key, List<T> list) {
        String value = JSONUtil.toJson(list);
        return set(key,value);
    }

    public <T> List<T> getList(String key, Class<T> clz) {
        String json = get(key);
        if(json!=null){
            List<T> list = JSONUtil.toList(json, clz);
            return list;
        }
        return null;
    }

    public long lpush(final String key, Object obj) {
        final String value = JSONUtil.toJson(obj);
        long result = rt.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = rt.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    public long rpush(final String key, Object obj) {
        final String value = JSONUtil.toJson(obj);
        long result = rt.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = rt.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    public String lpop(final String key) {
        String result = rt.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = rt.getStringSerializer();
                byte[] res =  connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }


    /**
     * 用户注册去重检验 （利用redis加锁 解锁）
     * @param customerLogin
     */
//    public void customerSignCheck(CustomerLogin customerLogin){
//        //appId+customerId存入 redis
//        //查询redis里面是否有记录， 如果存在就 Thread.sleep(100)若干次; 如果没有就 执行业务代码
//
//        Long customerId = customerLogin.getCustomerId();
//
//        String appId = customerLogin.getAppId();
//
//        for(int i = 1;i<20;i++) {
//            //如果"appId+customerId"存在返回false；如果"appId+customerId"不存在，就存入reidis返回true
//            if (!redisTemplate.opsForValue().setIfAbsent(appId+customerId.toString(), "customerSignCheck")) {
//
//                try {
//                    logger.info("redis中第 {} 次,检测到 appId+customerId={} 的用户已锁定，开始休眠",i,appId+customerId.toString());
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }else{
//                logger.info("redis中第 {} 次,检测到 appId+customerId={} 的用户不存在，开始锁定资源",i,appId+customerId.toString());
//                break;
//            }
//        }
//        try {
//            CustomerSign cusotmerSign = customerSignService.findByCustomerIdAndAppId(customerId,appId);
//
//            if (cusotmerSign == null) {
//
//                cusotmerSign = BeanUtils.toCustomerSign(customerLogin);
//
//                MatchResult registermatch = matchService.match(cusotmerSign);
//                //BI推送KAFKA
//                kafkaProducerService.getRegisterChanStorm(registermatch);
//            }
//        }catch(Exception e){
//
//        }finally {
//            logger.info("redis中检测到 appId+customerId={} 的用户准备释放资源",appId+customerId.toString());
//            redisTemplate.delete(appId+customerId.toString());
//        }
//
//    }




}
