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
 *  3、单线程的redis为什么这么快 - 内部实现采用epoll
 *      分析:这个问题其实是对redis内部机制的一个考察。其实根据博主的面试经验，很多人其实都不知道redis是 单线程 工作模型。所以，这个问题还是应该要复习一下的。
 *      回答:主要是以下几点
 *           1)完全基于内存，类似于HashMap，HashMap的优势就是查找和操作的时间复杂度都是O(1)；
 *           2)数据结构简单
 *           3)采用单线程，避免了不必要的上下文切换和竞争条件，不用去考虑各种锁的问题，不存在加锁释放锁操作
 *           4)使用IO多路复用模型，非阻塞IO；- IO多路复用模型是利用 select、poll、epoll 可以同时监察多个流的 I/O 事件的能力
 *                                        - 这里“多路”指的是多个网络连接
 *                                        - “复用”指的是复用同一个线程。
 *           5)使用底层模型不同，Redis直接自己构建了VM 机制 。
 *
 *
 *
 *
 *《可能是目前最详细的Redis内存模型及应用解读》：- 本文主要介绍以3.0为例的Redis的内存模型
 *   - 包括：Redis占用内存的情况及如何查询、不同的对象类型在内存中的编码方式、内存分配器（jemalloc）、简单动态字符串（SDS）、RedisObject等。
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
 *         2、jemalloc - Redis在编译时便会指定内存分配器；内存分配器可以是 libc 、jemalloc或者tcmalloc，默认是 jemalloc。
 *              - jemalloc作为Redis的默认内存分配器，在减小内存碎片方面做的相对比较好。
 *         3、RedisObject - 前面说到，Redis对象有5种类型；无论是哪种类型，Redis都不会直接存储，而是通过 RedisObject对象 进行存储。
 *              - RedisObject的每个字段的含义和作用如下：
 *                 （1）type - type字段表示对象的类型，占4个比特；目前包括 (字符串)、(列表)、(哈希)、（集合)、(有序集合)。
 *                          - 命令： type key
 *                 （2）encoding - encoding表示对象的内部编码，占4个比特。- 对于Redis支持的每种类型，都有至少两种内部编码，例如对于字符串，有int、embstr、raw三种编码。
 *                          - 命令： object encoding key - 通过object encoding命令，可以查看对象采用的编码方式
 *                 （3）lru - lru记录的是对象最后一次被命令程序访问的时间，占据的比特数不同的版本有所不同（如4.0版本占24比特，2.6版本占22比特）
 *                         - 命令：object idletime key 命令可以显示该空转时间（单位是秒）
 *                 （4）refcount - refcount记录的是该对象被引用的次数，类型为整型。
 *                         - refcount的作用，主要在于对象的引用计数和内存回收：- 当创建新对象时，refcount初始化为1；
 *                                                                       - 当有新程序使用该对象时，refcount加1；
 *                                                                       - 当对象不再被一个新程序使用时，refcount减1；
 *                                                                       - 当refcount变为0时，对象占用的内存会被释放。
 *                         - Redis中被多次使用的对象(refcount>1)称为共享对象。目前共享对象仅支持整数值的字符串对象。
 *                 （5）ptr - ptr指针指向具体的数据，如前面的例子中，set hello world，ptr指向包含字符串world的SDS。
 *                 （6）总结 - 综上所述，redisObject的结构与对象类型、编码、内存回收、共享对象都有关系；一个redisObject对象的大小为16字节：
 *                          - 4bit+4bit+24bit+4Byte+8Byte=16Byte。
 *         4、SDS - Redis没有直接使用C字符串(即以空字符‘’结尾的字符数组)作为默认的字符串表示，而是使用了SDS。SDS是简单动态字符串(Simple Dynamic String)的缩写
 *              （1）SDS结构 - 其中，buf表示字节数组，用来存储字符串；len表示buf已使用的长度，free表示buf未使用的长度。
 *              （2）SDS与C字符串的比较
 *                      - SDS在C字符串的基础上加入了free和len字段，带来了很多好处：
 *                          - 获取字符串长度：SDS是O(1)，C字符串是O(n)。
 *
 *      四、Redis的对象类型与内部编码 - 前面已经说过，Redis支持5种对象类型，而每种结构都有至少两种编码。
 *          - 这样做的好处在于：1、一方面接口与实现分离，当需要增加或改变内部编码时，用户使用不受影响
 *                           2、另一方面可以根据不同的应用场景切换内部编码，提高效率。
 *          - 关于Redis内部编码的转换，都符合以下规律：编码转换在Redis写入数据时完成，且转换过程 不可逆，只能从小内存编码向大内存编码转换。
 *
 *         1、字符串
 *           （1）概况 - 字符串是最基础的类型，因为所有的键都是字符串类型，且字符串之外的其他几种复杂类型的元素也是字符串。字符串长度不能超过512MB。
 *           （2）内部编码 - 字符串类型的内部编码有3种，它们的应用场景如下：
 *                          1、int：8个字节的长整型。字符串值是整型时，这个值使用long整型表示。
 *                          2、embstr：<=39字节的字符串。embstr与raw都使用RedisObject和SDS保存数据。
 *                          3、raw：大于39个字节的字符串
 *           （3）编码转换 - 当int数据不再是整数，或大小超过了long的范围时，自动转化为raw。
 *                       - 而对于embstr，由于其实现是只读的，因此在对embstr对象进行修改时，都会先转化为raw再进行修改，
 *                         因此，只要是修改embstr对象，修改后的对象一定是raw的，无论是否达到了39个字节。
 *         2、列表
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
