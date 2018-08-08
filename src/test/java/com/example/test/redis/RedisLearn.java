package com.example.test.redis;

/**
 * @Auther: gsh
 * @Date: 2018/8/6 11:44
 * @Description:   学会这15点，让你分分钟拿下Redis数据库
 *
 * 1、Redis简介 - REmote DIctionary Server(Redis) - 远程字典服务
 *    - Redis是一个开源的使用ANSI C语言编写的Key-Value数据库，并提供多种语言的API。
 * 2、支持的语言 - 大部门都支持
 * 3、Redis的应用场景到底有哪些？
 *      1、最常用的就是会话缓存
 *      2、消息队列，比如支付
 *      3、活动排行榜或计数
 *      4、发布、订阅消息(消息通知)
 *      5、商品列表、评论列表等
 * 4、Redis安装 - Redis是c语言开发的，安装redis需要c语言的编译环境
 * 5、Redis数据类型 - Redis一共支持五种数据类型： 6、Stream
 *      1、string(字符串) - 它是redis最基本的数据类型，一个key对应一个value，需要注意是一个键值较大存储512MB，而memcache只有1MB
 *          相关命令介绍  - set 为一个Key设置value（值）
 *                      - get 获得某个key对应的value（值）
 *                      - getset 为一个Key设置value（值）并返回对应的值
 *                      - mset 为多个key设置value（值）
 *      2、hash(哈希) - redis hash是一个键值对的集合， 是一个string类型的field和value的映射表，适合用于存储对象
 *          相关命令介绍 - hset 将Key对应的hash中的field配置为value，如果hash不存则自动创建，
 *                     - hget 获得某个hash中的field配置的值
 *                     - hmset 批量配置同一个hash中的多个field值
 *                     - hmget 批量获得同一个hash中的多个field值
 *      3、list(列表) - 是redis简单的字符串列表，它按插入顺序排序
 *          相关命令介绍 - lpush 向指定的列表左侧插入元素，返回插入后列表的长度
 *                     - rpush 向指定的列表右侧插入元素，返回插入后列表的长度
 *                     - llen 返回指定列表的长度
 *                     - lrange 返回指定列表中指定范围的元素值
 *      4、set(集合) - 是string类型的无序集合，也不可重复
 *          相关命令介绍 - sadd 添加一个string元素到key对应的set集合中，成功返回1，如果元素存在返回0
 *                     - smembers 返回指定的集合中所有的元素
 *                     - srem 删除指定集合的某个元素
 *      5、zset（sorted set 有序集合）- 是string类型的有序集合，也不可重复.
 *          - sorted set中的每个元素都需要指定一个分数，根据分数对元素进行升序排序，如果多个元素有相同的分数，则以字典序进行升序排序，sorted set 因此非常适合实现排名
 *          相关命令介绍 - zadd 向指定的sorteset中添加1个或多个元素
 *                     - zrem 从指定的sorteset中删除1个或多个元素
 *                     - zcount 查看指定的sorteset中指定分数范围内的元素数量
 *                     - zscore 查看指定的sorteset中指定分数的元素
 *                     - zrangebyscore 查看指定的sorteset中指定分数范围内的所有元素
 * 6、键值相关的命令 - exists #确认key是否存在
 *                - del #删除key
 *                - expire #设置Key过期时间(单位秒)
 *                - persist #移除Key过期时间的配置
 *                - rename #重命名key
 *                - type #返回值的类型
 * 7、Redis服务相关的命令 - slect #选择数据库(数据库编号0-15)
 *                     - quit #退出连接
 *                     - info #获得服务的信息与统计
 *                     - monitor #实时监控
 *                     - config get #获得服务配置
 *                     - flushdb #删除当前选择的数据库中的key
 *                     - flushall #删除所有数据库中的key
 * 8、Redis的发布与订阅 - Redis发布与订阅(pub/sub)是它的一种消息通信模式，一方发送信息，一方接收信息。
 * 9、Redis事务 - Redis事务可以一次执行多条命令
 *          1、发送exec命令前放入队列缓存，结束事务
 *          2、收到exec命令后执行事务操作，如果某一命令执行失败，其它命令仍可继续执行
 *          3、一个事务执行的过程中，其它客户端提交的请求不会被插入到事务执行的命令列表中
 *    一个事务经历三个阶段:1、开始事务(命令:multi)
 *                      2、命令执行
 *                      3、结束事务(命令:exec)
 * 10、Redis安全配置 - 可以通过修改配置文件设备密码参数来提高安全性 - #requirepass foobared - 去掉注释#号就可以配置密码
 * 11、Redis持久化 - Redis持久有两种方式:1、Snapshotting(快照), 2、Append-only file(AOF)
 *      - Snapshotting(快照)：1、将存储在内存的数据以快照的方式写入二进制文件中，如默认dump.rdb中
 *                           2、save 900 1  #900秒内如果超过1个Key被修改，则启动快照保存
 *                           3、save 300 10  #300秒内如果超过10个Key被修改，则启动快照保存
 *                           4、save 60 10000    #60秒内如果超过10000个Key被修改，则启动快照保存
 *      - Append-only file(AOF)：1、使用AOF持久化时，服务会将每个收到的写命令通过write函数追加到文件中（appendonly.aof）
 *                              2、AOF持久化存储方式参数说明：
 *                                  appendonly yes  #开启AOF持久化存储方式
 *                                  appendfsync always  #收到写命令后就立即写入磁盘，效率最差，效果较好
 *                                  appendfsync everysec    #每秒写入磁盘一次，效率与效果居中
 *                                  appendfsync no  #完全依赖OS，效率较佳，效果没法保证
 * 12、Redis 性能测试 - 自带相关测试工具
 *      [root@test ~]# redis-benchmark --help
 * 13、Redis的备份与恢复 - Redis自动备份有两种方式：第一种是通过dump.rdb文件实现备份、第二种使用aof文件实现自动备份
 *      1、第一种是通过dump.rdb文件实现备份 - Redis服务默认的自动文件备份方式(AOF没有开启的情况下)，在服务启动时，就会自动从dump.rdb文件中去加载数据。
 *          - 具体配置在redis.conf：save 900 1/save 300 10/save 60 10000
 *          - 也可以手工执行save命令实现手动备份，- SAVE命令表示使用主进程将当前数据库快照到dump文件
 *                                           - BGSAVE命令表示，主进程会fork一个子进程来进行快照备份
 *                                           - 两种备份不同之处，前者会阻塞主进程，后者不会。
 *          - redis快照到dump文件时，会自动生成dump.rdb的文件
 *      2、第二种使用aof文件实现自动备份 - redis服务默认是关闭此项配置
 *          - AOF文件备份，是备份所有的历史记录以及执行过的命令，和mysql binlog很相似，在恢复时就是重新执次一次之前执行的命令，
 *            需要注意的就是在恢复之前，和数据库恢复一样需要手工删除执行过的del或误操作的命令。
 *          - AOF与dump备份不同：
 *              1、aof文件备份与dump文件备份不同
 *              2、服务读取文件的优先顺序不同，会按照以下优先级进行启动
 *                  - 如果只配置AOF,重启时加载AOF文件恢复数据
 *                  - 如果同时 配置了RDB和AOF,启动是只加载AOF文件恢复数据
 *                  - 如果只配置RDB,启动时将加载dump文件恢复数据
 *                  注意：只要配置了aof，但是没有aof文件，这个时候启动的数据库会是空的
 * 14、Redis 生产优化介绍 -
 *     1、Master最好不要做任何持久化工作，如RDB内存快照和AOF日志文件
 *     2、如果数据比较重要，某个Slave开启AOF备份数据，策略设置为每秒同步一次
 *     3、为了主从复制的速度和连接的稳定性，Master和Slave最好在同一个局域网内
 *     4、主从复制不要用图状结构，用单向链表结构更为稳定，即：Master <- Slave1 <- Slave2 <- Slave3...
 *        - 这样的结构方便解决单点故障问题，实现Slave对Master的替换。如果Master挂了，可以立刻启用Slave1做Master，其他不变。
 * 15、Redis集群应用 -
 *     - 为了避免Master DB的单点故障，集群一般都会采用两台Master DB做双机热备，所以整个集群的读和写的可用性都非常高。
 *
 * 16、Redis的回收策略 -
 *     1、volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选 最近最少使用的数据淘汰
 *     2、volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
 *     3、volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
 *     4、allkeys-lru：从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
 *     5、allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
 *     6、no-enviction（驱逐）：禁止驱逐数据
 *
 * 17、使用Redis有哪些好处？
 *     1、速度快，因为数据存在内存中，类似于 HashMap，HashMap的优势就是查找和操作的时间复杂度都是O(1)
 *     2、支持丰富数据类型，支持string，list，set，sorted set，hash
 *     3、redis可以持久化其数据
 *     4、支持事务，操作都是原子性，所谓的原子性就是对数据的更改要么全部执行，要么全部不执行
 *     5、丰富的特性：可用于缓存，消息，按key设置过期时间，过期后将会自动删除
 * 18、
 *
 */
public class RedisLearn {
}
