package com.example.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by gsh on 18/4/8.
 *
 *  为什么分布式一定要有redis?
 *
 *  1、为什么使用redis
 *      分析:博主觉得在项目中使用redis，主要是从两个角度去考虑:性能 和 并发。
 *
 *     （一）性能
 *      我们在碰到需要执行耗时特别久，且结果不频繁变动的SQL，就特别适合将运行结果 放入缓存。这样，后面的请求就去缓存中读取，使得请求能够迅速响应。
 *
 *     （二）并发
 *     在大并发的情况下，所有的请求直接访问数据库，数据库会出现连接异常。这个时候，就需要使用redis做一个缓冲操作，让请求先访问到redis，而不是直接访问数据库。
 *
 *  2、使用redis有什么缺点
 *      回答:主要是四个问题
         (一)缓存和数据库双写一致性问题
         (二)缓存雪崩问题
         (三)缓存击穿问题
         (四)缓存的并发竞争问题
 *
 *  3、单线程的redis为什么这么快
 *      分析:这个问题其实是对redis内部机制的一个考察。其实根据博主的面试经验，很多人其实都不知道redis是 单线程 工作模型。所以，这个问题还是应该要复习一下的。
 *      回答:主要是以下三点
             (一)纯内存操作
             (二)单线程操作，避免了频繁的上下文切换
             (三)采用了非阻塞I/O多路复用机制
 *
 *
 *
 *
 *
 */
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String master;

    private String host1;

    private String host2;

    private String host3;

    private String auth;

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    protected JedisPoolConfig getJedisPoolConfig(){
        return new JedisPoolConfig();
    }



    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        return connectionFactory;
    }

//    @Bean
//    @ConfigurationProperties(prefix = "spring.redis")
//    public RedisConnectionFactory redisSentialConnectionFactory(JedisPoolConfig jedisPoolConfig) {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(master)
//                .sentinel(host1, 26379)
//                .sentinel(host2, 26379)
//                .sentinel(host3,26379);
//
//        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(sentinelConfig);
//        connectionFactory.setPassword(auth);
//        return connectionFactory;
//    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,String > redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }



    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }


    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public String getHost2() {
        return host2;
    }

    public void setHost2(String host2) {
        this.host2 = host2;
    }

    public String getHost3() {
        return host3;
    }

    public void setHost3(String host3) {
        this.host3 = host3;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
