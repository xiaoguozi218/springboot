package com.example.dao;

/**
 * Created by gsh on 2018/7/19.
 *
 *《MySQL扩展具体的实现方式》：关于数据库的扩展主要包括：1、业务拆分 2、主从复制 3、数据库分库与分表。
 * 一、业务拆分 - 由原来的单体应用根据业务进行拆分，进行微服务改造，每一个模块都使用单独的数据库来进行存储，不同的业务访问不同的数据库，系统的吞吐量自然就提高了。
 * 二、主从复制 -
 *    - 主要讲述了MySQL主从复制的原理：数据复制的实际就是Slave从Master获取Binary log文件，然后在 本地镜像 的执行日志中记录的操作。
 *                                 由于主从复制的过程是异步的，因此Slave和Master之间的数据 有可能存在延迟 的现象，此时只能保证数据 最终的一致性。
 * 三、数据库分库与分表
 *  1、分表实现策略 - 关键字：用户ID、表容量
 *     - 对于大部分数据库的设计和业务的操作基本都与用户的ID相关，因此使用用户ID是最常用的分表的路由策略。用户的ID可以作为贯穿整个系统用的重要字段。
 *       因此，使用用户的ID我们不仅可以方便我们的查询，还可以将数据平均的分配到不同的数据库中。
 *    - 接着上述电商平台假设，订单表order存放用户的订单数据 -
 *      当数据比较大的时候，对数据进行分表操作，首先要确定需要将数据平均分配到多少张表中，也就是：表容量。
 *      这里假设有100张表进行存储，则我们在进行存储数据的时候，首先对用户ID进行取模操作，根据 user_id%100 获取对应的表进行存储查询操作，
 * 注意：在实际的开发中，如果你使用MyBatis做持久层的话，MyBatis已经提供了很好得支持数据库分表的功能，例如上述sql用MyBatis实现的话应该是：
 *
 *  2、分库实现策略 - 用户ID、库容量
 *     - 数据库分表能够解决单表数据量很大的时候数据查询的效率问题，但是无法给数据库的并发操作带来效率上的提高，
 *       因为分表的实质还是在一个数据库上进行的操作，很容易受数据库IO性能的限制。
 *     - 分库策略与分表策略的实现很相似，最简单的都是可以通过 取模的方式 进行路由。
 *
 * 四、分库与分表实现策略
 *      - 上述的配置中，数据库分表可以解决 单表海量数据的查询性能问题，分库可以解决 单台数据库的并发访问压力问题。
 *      - 有时候，我们需要同时考虑这两个问题，因此，我们既需要对单表进行分表操作，还需要进行分库操作，以便同时扩展系统的并发处理能力和提升单表的查询性能，就是我们使用到的分库分表。
 *      - 分库分表的策略相对于前边两种复杂一些，一种常见的路由策略如下：
 *          １、中间变量　＝ user_id%（库数量*每个库的表数量）;
 *          ２、库序号　＝　取整（中间变量／每个库的表数量）; - 取整
 *          ３、表序号　＝　中间变量％每个库的表数量;       - 取模
 *       例如：数据库有256 个，每一个库中有1024个数据表，用户的user_id＝262145，按照上述的路由策略，可得：这样的话，对于user_id＝262145，将被路由到第０个数据库的第１个表中。
 *          １、中间变量　＝ 262145%（256*1024）= 1;
 *          ２、库序号　＝　取整（1／1024）= 0;
 *          ３、表序号　＝　1％1024 = 1;
 *
 *
 * 注意：
 *  - 最后需要指出的是，分库分表目前有很多的中间件可供选择，最常见的是使用淘宝的中间件Cobar。  还有MyCat
 *  - 上述的分库和分表操作，查询性能和并发能力都得到了提高，但是还有一些需要注意的就是，例如：1、原本跨表的事物变成了分布式事物；2、关联查询麻烦 3、数据迁移也变得麻烦了
 *  -
 *
 *
 *《数据库索引为什么使用B树？》 - 其中的B就表示平衡(Balance)
 *  1、名次解释：- B tree： 二叉树（Binary tree），每个节点只能存储一个数。
 *             - B-tree：B树（B-Tree，并不是B“减”树，横杠为连接符，容易被误导）B树属于多叉树又名 平衡多路查找树。每个节点可以多个数（由磁盘大小决定）。
 *             - B+tree 和 B*tree 都是 B-tree的变种
 *  2、索引为什么是用B树呢？
 *    一般来说，索引本身也很大，不可能全部存储在内存中，因此索引往往以索引文件的形式存储的磁盘上。这样的话，索引查找过程中就要产生磁盘I/O消耗，相对于内存存取，I/O存取的消耗要高几个数量级，
 *    所以评价一个数据结构作为索引的优劣最重要的指标就是在查找过程中磁盘I/O操作次数的渐进复杂度。换句话说，索引的结构组织要 尽量减少查找过程中磁盘I/O的存取次数。
 *    而B-/+/*Tree，经过改进可以有效的利用系统对磁盘的 块读取特性，在读取相同磁盘块的同时，尽可能多的加载索引数据，来提高索引命中效率，从而达到减少磁盘IO的读取次数。
 *    2.1 - B-tree
 *        - B-tree 利用了 磁盘块 的特性进行构建的树。每个磁盘块一个节点，每个节点包含了很关键字。把树的节点关键字增多后树的层级比原来的二叉树少了，减少数据查找的次数和复杂度。
 *        - B-tree 巧妙利用了磁盘预读原理，将一个节点的大小设为等于一个页（每页为4K），这样每个节点只需要一次I/O就可以完全载入。B-tree 的数据可以存在任何节点中。
 *    2.2 - B+tree - B+tree 是 B-tree 的变种，数据只能存储在 叶子节点。
 *        - B+tree 是 B-tree 的变种，B+tree 数据只存储在叶子节点中。这样在B树的基础上每个节点存储的关键字数更多，树的层级更少所以查询数据更快，
 *          所有指关键字指针都存在叶子节点，所以每次查找的次数都相同所以查询速度更稳定;
 *    2.3 - B*tree
 *        - B*tree 每个磁盘块中又添加了对下一个磁盘块的引用。这样可以在当前磁盘块满时，不用扩容直接存储到下一个临近磁盘块中。
 *          当两个邻近的磁盘块都满时，这两个磁盘块各分出1/3的数据重新分配一个磁盘块，这样这三个磁盘块的数据都为2/3。
 *        - B*tree在B+树的基础上因其初始化的容量变大，使得节点空间使用率更高，而又存有兄弟节点的指针，可以向兄弟节点转移关键字的特性使得B*树额分解次数变得更少；
 *
 *《MySQL的btree索引和hash索引的区别》：
 *      - 索引是帮助mysql获取数据的数据结构。最常见的索引是Btree索引和Hash索引。
 *      - 不同的引擎对于索引有不同的支持：Innodb和MyISAM默认的索引是Btree索引； 而Mermory默认的索引是Hash索引。
 *  1、hash 索引结构的特殊性，其检索效率非常高，索引的检索可以一次定位，不像B-Tree 索引需要从根节点到支节点，最后才能访问到叶子节点这样多次的IO访问，
 *     所以 Hash 索引的查询效率要远高于 B-Tree 索引。
 *  2、可能很多人又有疑问了，既然 Hash 索引的效率要比 B-Tree 高很多，为什么大家不都用 Hash 索引而还要使用 B-Tree 索引呢？
 *    任何事物都是有两面性的，Hash 索引也一样，虽然 Hash 索引效率高，但是 Hash 索引本身由于其特殊性也带来了很多限制和弊端，主要有以下这些。
 *  3、Hash索引的缺点：
 *      1、因为Hash索引比较的是经过Hash计算的值，所以只能进行等式比较，不能用于范围查询
 *      2、每次都要全表扫描
 *      3、由于哈希值是按照顺序排列的，但是哈希值映射的真正数据在哈希表中就不一定按照顺序排列，所以无法利用Hash索引来加速任何 排序操作
 *      4、不能用部分索引键来搜索，因为组合索引在计算哈希值的时候是一起计算的。
 *      5、Hash 索引遇到大量Hash值相等的情况后性能并不一定就会比B-Tree索引高。
 *
 *  4、Btree索引：至于Btree索引，它是以B+树为存储结构实现的。 - 但是Btree索引的存储结构在 Innodb 和 MyISAM 中有很大区别。
 *      - MyISAM：在 MyISAM中 数据文件和索引文件 是分开的。- 因此MyISAM的索引方式也称为 非聚集，Innodb的索引方式成为 聚集索引。
 *      - Innodb：Innodb的 索引文件就是数据文件
 *
 *《数据库索引之稠密索引和稀疏索引》
 *  1、稠密索引 - 在稠密索引中文件中的每个搜索码值都对应一个索引值。索引项包括索引值以及指向该搜索码值的第一条数据记录的指针。由于该索引符合 聚集索引，因此记录根据相同的码值排序。
 *  2、稀疏索引 - 在稀疏索引中，只为索引码的某些值建立索引项。同理因为稀疏索引也是聚集索引。每一个索引项包括索引值以及指向该搜索码值的第一条数据记录的指针。
 *  3、两者优缺点：- 1.稠密索引比稀疏索引更快地定位一条记录。
 *               - 2.稀疏索引所占空间小，并且插入和删除时所需的维护开销也小。
 *
 *《数据库分库分表如何避免“过度设计”和“过早优化”》
 *  一、数据切分
 *      - 关系型数据库本身比较容易成为系统瓶颈，单机存储容量、连接数、处理能力都有限。当单表的数据量达到1000W或100G以后，由于查询维度较多，即使添加从库、优化索引，做很多操作时性能仍下降严重。
 *        此时就要考虑对其进行切分了，切分的目的就在于减少数据库的负担，缩短查询时间。
 *      - 数据库分布式核心内容无非就是 数据切分（Sharding），以及切分后对数据的定位、整合。
 *        数据切分就是将数据分散存储到多个数据库中，使得单一数据库中的数据量变小，通过扩充主机的数量缓解单一数据库的性能问题，从而达到提升数据库操作性能的目的。
 *      - 数据切分根据其切分类型，可以分为两种方式：- 1、垂直（纵向）切分
 *                                           - 2、水平（横向）切分
 *      1、垂直（纵向）切分 - 垂直切分常见有 垂直分库 和 垂直分表 两种
 *         - 垂直分库就是根据业务耦合性，将关联度低的不同表存储在不同的数据库。做法与大系统拆分为多个小系统类似，按业务分类进行独立划分。与“微服务治理”的做法相似，每个微服务使用单独的一个数据库。
 *         - 垂直切分的 优点：1、解决业务系统层面的耦合，业务清晰；
 *                        2、与微服务的治理类似，也能对不同业务的数据进行分级管理、维护、监控、扩展等；
 *                        3、高并发场景下，垂直切分一定程度的提升IO、数据库连接数、单机硬件资源的瓶颈。
 *                    缺点：1、部分表无法join，只能通过接口聚合方式解决，提升了开发的复杂度；
 *                         2、分布式事务处理复杂；
 *                         3、依然存在单表数据量过大的问题（需要水平切分）。
 *      2、水平（横向）切分 - 水平切分分为 库内分表 和 分库分表
 *          - 当一个应用难以再细粒度的垂直切分，或切分后数据量行数巨大，存在单库读写、存储性能瓶颈，这时候就需要进行水平切分了。
 *          - 库内分表只解决了单一表数据量过大的问题,但没有将表分布到不同机器的库上,因此对于减轻MySQL数据库的压力来说,帮助不是很大,大家还是竞争同一个物理机的CPU、内存、网络IO，最好通过分库分表来解决。
 *          - 水平切分的 优点：1、不存在单库数据量过大、高并发的性能瓶颈，提升系统稳定性和负载能力；
 *                          2、应用端改造较小，不需要拆分业务模块。
 *                     缺点：1、跨分片的事务一致性难以保证；
 *                          2、跨库的join关联查询性能较差；
 *                          3、数据多次扩展难度和维护量极大。
 *          - 水平切分后同一张表会出现在多个数据库/表中，每个库/表的内容不同。几种典型的数据分片规则为：
 *              1、根据数值范围 - 按照时间区间或ID区间来切分。
 *              2、根据数值取模 - 一般采用hash取模mod的切分方式。
 *
 *  二、分库分表带来的问题 -
 *      - 分库分表能有效的缓解单机和单库带来的性能瓶颈和压力，突破网络IO、硬件资源、连接数的瓶颈，同时也带来了一些问题。下面将描述这些技术挑战以及对应的解决思路。
 *      1、事务一致性问题
 *          1）分布式事务 - 当更新内容同时分布在不同库中，不可避免会带来跨库事务问题。跨分片事务也是分布式事务，没有简单的方案，一般可使用“XA协议”和“两阶段提交”处理。
 *                      - 分布式事务能最大限度保证数据库操作的原子性。但在提交事务时需要协调多个节点，推后了提交事务的时间点，延长了事务的执行时间，导致事务在访问共享资源时发生冲突或死锁的概率增高。
 *                      - 随着数据库节点的增多，这种趋势会越来越严重，从而成为系统在数据库层面上水平扩展的枷锁。
 *         2）最终一致性 - 对于那些性能要求很高，但对一致性要求不高的系统，往往不苛求系统的实时一致性，只要在允许的时间段内达到最终一致性即可，可采用 事务补偿 的方式。
 *                     - 与事务在执行中发生错误后立即回滚的方式不同，事务补偿是一种事后检查补救的措施，一些常见的实现方法有：对数据进行对账检查；基于日志进行对比；定期同标准数据来源进行同步等。
 *                       事务补偿还要结合业务系统来考虑。
 *     2、跨节点关联查询 join 问题 - 切分之后，数据可能分布在不同的节点上，此时join带来的问题就比较麻烦了，考虑到性能，尽量避免使用join查询。
 *          解决这个问题的一些方法：
 *        1）全局表？ - 全局表，也可看做是“数据字典表”，就是系统中所有模块都可能依赖的一些表，为了避免跨库join查询，可以将这类表在每个数据库中都保存一份。
 *                   这些数据通常很少会进行修改，所以也不担心一致性的问题。
 *        2）字段冗余 - 一种典型的反范式设计，利用空间换时间，为了性能而避免join查询。例如：订单表保存userId时候，也将userName冗余保存一份，这样查询订单详情时就不需要再去查询“买家user表”了。
 *                  - 但这种方法适用场景也有限，比较适用于依赖字段比较少的情况。而冗余字段的数据一致性也较难保证，就像上面订单表的例子，买家修改了userName后，是否需要在历史订单中同步更新呢？
 *                    这也要结合实际业务场景进行考虑。
 *        3）数据组装 - 在系统层面，分两次查询，第一次查询的结果集中找出关联数据id，然后根据id发起第二次请求得到关联数据。最后将获得到的数据进行字段拼装。
 *     3、跨节点分页、排序、函数问题 - 跨节点多库进行查询时，会出现limit分页、order by排序等问题。
 *     4、全局主键避重问题 - 在分库分表环境中，由于表中数据同时存在不同数据库中，主键值平时使用的自增长将无用武之地，某个分区数据库自生成的ID无法保证全局唯一。
 *        - 因此需要单独设计全局主键，以避免跨库主键重复问题。有一些常见的主键生成策略：
 *           1）UUID
 *           2）结合数据库维护主键ID表
 *              - 使用 MyISAM 存储引擎而不是 InnoDB，以获取更高的性能。MyISAM使用的是 表级别的锁，对表的读写是串行的，所以不用担心在并发时两次读取同一个ID值。
 *           3）Snowflake分布式自增ID算法 - 雪花算法
 *              - Twitter的snowflake算法解决了分布式系统生成全局ID的需求，生成64位的Long型数字，组成部分：
 *                  1、第一位未使用
 *                  2、接下来41位是毫秒级时间，41位的长度可以表示69年的时间
 *                  3、5位datacenterId，5位workerId。10位的长度最多支持部署1024个节点
 *                  4、最后12位是毫秒内的计数，12位的计数顺序号支持每个节点每毫秒产生4096个ID序列
 *              - 这样的好处是：毫秒数在高位，生成的ID整体上按时间趋势递增；不依赖第三方系统，稳定性和效率较高，理论上QPS约为409.6w/s（1000*2^12），
 *                            并且整个分布式系统内不会产生ID碰撞；可根据自身业务灵活分配bit位。
 *              - 不足就在于：强依赖机器时钟，如果时钟回拨，则可能导致生成ID重复。
 *      5、数据迁移、扩容问题
 *          - 当业务高速发展，面临性能和存储的瓶颈时，才会考虑分片设计，此时就不可避免的需要考虑历史数据迁移的问题。一般做法是先读出历史数据，然后按指定的分片规则再将数据写入到各个分片节点中。
 *          - 此外，还需要根据当前的数据量和QPS，以及业务发展的速度，进行容量规划，推算出大概需要多少分片（一般建议单个分片上的单表数据量不超过1000W）。
 *          - 如果采用数值范围分片，只需要添加节点就可以进行扩容了，不需要对分片数据迁移。如果采用的是数值取模分片，则考虑后期的扩容问题就相对比较麻烦。
 *  三、什么时候考虑切分
 *      1、能不切分尽量不要切分 - 不到万不得已不用轻易使用分库分表这个大招，避免"过度设计"和"过早优化"。
 *          - 分库分表之前，不要为分而分，先尽力去做力所能及的事情，例如：升级硬件、升级网络、读写分离、索引优化等等。当数据量达到单表的瓶颈时候，再考虑分库分表。
 *      2、数据量过大，正常运维影响业务访问
 *      3、随业务发展需对某些字段垂直拆分
 *      4、数据量快速增长
 *          - 随着业务的快速发展，单表中的数据量会持续增长，当性能接近瓶颈时，就需要考虑水平切分，做分库分表了。此时一定要选择合适的切分规则，提前预估好数据容量。
 *      5、安全性和可用性 - 不要把鸡蛋放在一个篮子里。
 *          - 在业务层面上垂直切分，将不相关的业务的数据库分隔，因为每个业务的数据量、访问量都不同，不能因为一个业务把数据库搞挂而牵连到其他业务。
 *          - 利用水平切分，当一个数据库出现问题时，不会影响到100%的用户，每个库只承担业务的一部分数据，这样整体的可用性就能提高。
 *  四、案例分析
 *      1、用户中心业务场景
 *          - 用户中心是一个非常常见的业务，主要提供用户注册、登录、查询/修改等功能，其核心表为：
 *
 *《mysql常用命令大全》 - linux
 *  1、重启服务 - service mysqld restart
 *     查看状态 - service mysqld status
 *
 *
 */
public class DataBaseLearn {

}
