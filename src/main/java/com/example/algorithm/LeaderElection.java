package com.example.algorithm;
/**
 * @author  xiaoguozi
 * @create  2018/9/7 上午8:58
 * @desc    leader选举算法总结：zk选举算法 和 kafka选举算法
 *
 * 一、Zookeeper 的选举算法：
 *    1、leader的选择机制，zookeeper提供了三种方式：1）LeaderElection
 *                                           2）AuthFastLeaderElection
 *                                           3）FastLeaderElection - 默认的算法是FastLeaderElection
 *    2、大概可以分为三个阶段：- Phase 0: Leader election（选举阶段）
 *                       - Phase 1: Discovery（发现阶段）
 *                       - Phase 2: Synchronization（同步阶段）
 *      - 选举阶段：节点在一开始都处于选举阶段，只要有一个节点得到超半数节点的票数，它就可以当选准 leader。
 *                这一阶段的目的就是 为了选出一个准 leader，然后进入下一个阶段。协议并没有规定详细的选举算法，默认使用的 Fast Leader Election。
 *      - 发现阶段：在这个阶段，followers 跟准 leader 进行通信，同步 followers 最近接收的事务提议。这个一阶段的主要是三个目的：
 *              1、是发现当前大多数节点接收的最新提议，并且准 leader 生成新的 epoch，让 followers 接受，更新它们的 acceptedEpoch。
 *              2、当Follower 接受到来自准Leader 的 newEpoch 消息后，会检查当前的epoch 是否小于newEpoch，如果是就会重新赋值自己的epoch，
 *                 并且向leader反馈当前的Follower 的epoch，以及该Follower的历史事务集合。
 *              3、准 leader 接受到来自过半Follower的确认消息ack之后，准leader 就会从这些过半的服务器中选取一个Follower 集合，并使用该集合作为初始化集合，
 *                这个集合满足的最大epoch 与 zxid 都是所有集合中最大的（这一步骤最重要，用于同步阶段使用，同时也是zookeeper 的 leader挂机以后，新任leader 不会丢失事务的保证，也是ZAB算法与paxos算法的不同之处）
 *       - 同步阶段：同步阶段主要是利用 leader 前一阶段获得的最新提议历史，同步集群中所有的副本。只有当 quorum 都同步完成，准 leader 才会成为真正的 leader。follower 只会接收 zxid 比自己的 lastZxid 大的提议。
 *    3、zk的选举算法大概总结起来为以上几步，后续的广播阶段与选举无关，暂不提及。zk的选举算法之所以设计成以上几步，主要是为了保证分布式一致性。
 *      当leader阶段挂掉之后，新的leader会确保存在过半的Follower 已经提交了之前的leader 周期中的所有事务，发现阶段的引入，能够有效的保证 leader 在新的周期中提出事务之前，所有的进程都已经完成了对之前所有事务的提交。
 *
 *    - 下面给出几个名词定义：（1）Serverid：在配置server时，给定的服务器的标示id。
 *                       （2）Zxid:服务器在运行时产生的数据id，zxid越大，表示数据越新。
 *                       （3）Epoch：选举的轮数，即逻辑时钟。随着选举的轮数++
 *                       （4）Server状态：LOOKING,FOLLOWING,OBSERVING,LEADING
 * 二、kafka 选举算法：
 *    1、Kafka的Leader是什么？
 *      - 首先Kafka会将接收到的消息分区（partition），每个主题（topic）的消息有不同的分区。这样一方面消息的存储就不会受到单一服务器存储空间大小的限制，另一方面消息的处理也可以在多个服务器上并行。
 *      - 其次为了保证高可用，每个分区都会有一定数量的副本（replica）。这样如果有部分服务器不可用，副本所在的服务器就会接替上来，保证应用的持续性。
 *      - 但是，为了保证较高的处理效率，消息的读写都是在固定的一个副本上完成。这个副本就是所谓的Leader，而其他副本则是Follower。而Follower则会定期地到Leader上同步数据。
 *    2、Leader选举：关键点 - ISR、
 *      - 如果某个分区所在的服务器出了问题，不可用，kafka会从该分区的其他的副本中选择一个作为新的Leader。之后所有的读写就会转移到这个新的Leader上。现在的问题是应当选择哪个作为新的Leader。
 *        显然，只有那些跟Leader保持同步的Follower才应该被选作新的Leader。
 *      - Kafka会在Zookeeper上针对每个Topic维护一个称为ISR（in-sync replica，已同步的副本）的集合，该集合中是一些分区的副本。只有当这些副本都跟Leader中的副本同步了之后（看下面的注意点），kafka才会认为消息已提交，并反馈给消息的生产者。
 *        如果这个集合有增减，kafka会更新zookeeper上的记录。（注意：request.required.acks 这个参数有-1，0，1三个取值。只有设置为-1的时候，所有副本同步完成后才会返回给消息生产者。）
 *      - 如果某个分区的Leader不可用，Kafka就会从ISR集合中选择一个副本作为新的Leader。
 *      - 显然通过ISR，kafka需要的冗余度较低，可以容忍的失败数比较高。假设某个topic有f+1个副本，kafka可以容忍f个服务器不可用。
 *    3、为什么不用少数服从多数的方法？- 少数服从多数是一种比较常见的一致性算法和Leader选举法。它的含义是只有超过半数的副本同步了，系统才会认为数据已同步；选择Leader时也是从超过半数的同步的副本中选择。这种算法需要较高的冗余度。
 *                                 譬如只允许一台机器失败，需要有三个副本；而如果只容忍两台机器失败，则需要五个副本。而kafka的ISR集合方法，分别只需要两个和三个副本。
 *    4、如果所有的ISR副本都失败了怎么办？- 此时有两种方法可选：这两种方法各有利弊，实际生产中按需选择。
 *          1）一种是等待ISR集合中的副本复活。- 如果要等待ISR副本复活，虽然可以保证一致性，但可能需要很长时间。
 *          2）一种是选择任何一个立即可用的副本，而这个副本不一定是在ISR集合中。- 而如果选择立即可用的副本，则很可能该副本并不一致。
 *
 **/
public class LeaderElection {

}
