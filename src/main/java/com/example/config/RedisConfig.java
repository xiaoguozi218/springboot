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
 * 《*》可能是目前最详细的Redis内存模型及应用解读：- 本文主要介绍以3.0为例的Redis的内存模型
 *     一、Redis内存统计
 *        1、通过info命令可以查看内存使用情况：info memory
 *          - 其中，info命令可以显示redis服务器的许多信息，包括服务器基本信息、CPU、内存、持久化、客户端连接信息等等；memory是参数，表示只显示内存相关的信息。
 *
 *     二、Redis内存划分 - Redis的内存占用主要可以划分为以下几个部分：
 *        1、数据
 *          - 作为数据库，数据是最主要的部分，这部分占用的内存会统计在used_memory中。
 *          - Redis使用键值对存储数据，其中的值（对象）包括5种类型：字符串、哈希、列表、集合、有序集合。
 *          - 这5种类型是Redis对外提供的。实际上，在Redis内部，每种类型可能有2种或更多的内部编码实现。
 *            此外，Redis在存储对象时，并不是直接将数据扔进内存，而是会对对象进行各种包装：如RedisObject、SDS等。本文后面将重点介绍Redis中数据存储的细节。
 *        2、进程本身运行需要的内存
 *          - Redis主进程本身运行肯定需要占用内存，如代码、常量池等等。这部分内存大约几兆，在大多数生产环境中与Redis数据占用的内存相比可以忽略。
 *            这部分内存不是由jemalloc分配，因此不会统计在used_memory中。
 *        3、缓冲内存 - 缓冲内存包括：1、客户端缓冲区：存储客户端连接的输入输出缓冲；
 *                                2、复制积压缓冲区：用于部分复制功能；
 *                                3、AOF缓冲区：用于在进行AOF重写时，保存最近的写入命令。
 *                  - 在了解相应功能之前，不需要知道这些缓冲的细节。这部分内存由jemalloc分配，因此会统计在used_memory中。
 *        4、内存碎片
 *          - 内存碎片是Redis在分配、回收物理内存过程中产生的。 内存碎片不会统计在used_memory中。
 *
 *      三、Redis数据存储的细节
 *         1、概述 - 关于Redis数据存储的细节，涉及到内存分配器（如jemalloc）、简单动态字符串（SDS）、5种对象类型及内部编码、RedisObject。
 *                  在讲述具体内容之前，先说明一下这几个概念之间的关系。 下图是执行set hello world时，所涉及到的数据模型。
 *             （1）dictEntry：Redis是Key-Value数据库，因此对每个键值对都会有一个dictEntry，里面存储了指向Key和Value的指针；next指向下一个dictEntry，与本Key-Value无关。
 *             （2）Key：图中右上角可见，Key（“hello”）并不是直接以字符串存储，而是存储在SDS结构中。
 *             （3）redisObject：Value(“world”)既不是直接以字符串存储，也不是像Key一样直接存储在SDS中，而是存储在redisObject中。
 *                              实际上，不论Value是5种类型的哪一种，都是通过RedisObject来存储的；而RedisObject中的type字段指明了Value对象的类型，ptr字段则指向对象所在的地址。
 *                              不过可以看出，字符串对象虽然经过了RedisObject的包装，但仍然需要通过SDS存储。
 *             （4）jemalloc：无论是DictEntry对象，还是RedisObject、SDS对象，都需要内存分配器（如jemalloc）分配内存进行存储。
 *                           以DictEntry对象为例，有3个指针组成，在64位机器下占24个字节，jemalloc会为它分配32字节大小的内存单元。
 *         2、jemalloc
 *         3、RedisObject
 *         4、SDS
 *
 *      四、Redis的对象类型与内部编码
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
