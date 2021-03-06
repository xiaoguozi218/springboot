package com.example.concurrent;

/**
 * Created by MintQ on 2018/7/10.
 *
 * 《*》"分布式事务"解决方案汇总 -- 2PC/TCC/事务消息/1PC
 *      1、2PC ：   说到分布式事务，就会提到2pc。
 *          ~2PC简介：2pc涉及到2个阶段，3个操作： prepare/commit/rollback
 *                  阶段1：“准备提交”。事务协调者向所有参与者发起prepare，所有参与者回答yes/no。
 *                  阶段2：“正式提交”。如果所有参与者都回答yes，则向所有参与者发起commit；否则，向所有参与者发起rollback。
 *                      因此，要实现2pc，所有参与者，都得实现3个接口：prepare/commit/rollback。
 *          ~2PC的实现：关于2pc，对应的实现层面，也就是XA协议。有一个Atomikos开源库，也实现了这个协议。有兴趣的可以去看一下如何使用。
 *          ~2PC的问题：（1）阶段2，事务协调者挂了，则所有参与者接受不到commit/rollback指令，将处于“悬而不决”状态
 *                     （2）阶段2，其中一个参与者超时或者出错，那其他参与者，是commit，还是rollback呢？ 也不能确定
 *                    为了解决2pc的问题，又引入3pc。3pc有类似的挂了如何解决的问题，因此还是没能彻底解决问题，此处就不详述了。
 *      2、TCC ： 为了解决SOA系统中的分布式事务问题，支付宝提出了 TCC。2PC通常都是在跨库的DB层面，而TCC本质就是一个应用层面的2PC。
 *          ~同样，TCC中，每个参与者需要3个操作：Try/Confirm/Cancel，也是2个阶段。
 *                  阶段1：”资源预留/资源检查“，也就是事务协调者调用所有参与者的Try操作
 *                  阶段2：“一起提交”。如果所有的Try成功，一起执行Confirm。否则，所有的执行Cancel.
 *          ~TCC是如何解决2PC的问题呢？
 *              关键：Try阶段成功之后，Confirm如果失败(不管是协调者挂了，还是某个参与者超时），不断重试！！
 *              同样，Cancel失败了，也是不断重试。这就要求Confirm/Cancel都必须是幂等操作。
 *              ~下面以1个转账case为例，来说明TCC的过程：
 *                  有3个账号A, B, C，通过SOA提供的转账服务操作。A, B同时分别要向C转30, 50元，最后C的账号+80，A, B各减30, 50。
 *                  阶段1：A账号锁定30，B账号锁定50，检查C账号的合法性（比如C账号是否违法被冻结，C账号是否已注销。。。）。 所以，对应的“扣钱”的Try操作就是”锁定”，对应的“加钱”的Try操作就是检查账号合法性
 *                  阶段2：A, B, C都Try成功，执行Confirm。即A, B减钱，C加钱。如果任意一个失败，不断重试！
 *                  从上面的案例可以看出，Try操作主要是为了“保证业务操作的前置条件都得到满足”，然后在Confirm阶段，因为前置条件都满足了，所以可以不断重试保证成功。
 *
 *      3、事务消息 – 最终一致性
 *
 *      4、1PC – Saga – 事务补偿
 *          ~我们知道，在TCC里面，有2个阶段。其中第1个阶段是“锁定资源”，目的是为了保证第2个阶段的提交在业务上不会失败。
 *          ~而1pc，就是舍弃掉第1个阶段，不做资源锁定，直接进行第2个阶段的提交！如果业务的特性可以允许不需要锁定资源，那就可以省去第1个阶段，直接做第2个阶段。
 *          ~如果第2个阶段失败呢，有2种策略：策略1，同TCC一样，也是不断重试commit，硬着头皮上；
 *                                      策略2，回滚，也就是事务补偿，做之前操作的反操作。
 *
 *
 * 《*》微服务架构如何解决跨库问题的思路与方案？
 *      在数据分布在不同的数据库服务器的带来良好性能的同时，新的问题也随之而来，比如说数据一致性的保证，性能监控，数据存取复杂等，而较为突出的就是数据跨库问题！
 *          数据分布在不同的节点上，导致原来的连接查询需要跨库，字段的主键难以保证唯一，跨库的事务处理复杂，下面逐一解决:
 *      1、连接查询(join)问题: 因为库表分布在不同的机器上，连接查询失效。
 *         解决办法:①，代码解决:根据某个字段进行hash的方式进行分库分表，保证落在一个库中的类似表中(比如aa_00.t_user_0000和aa_00.t_member_0000)，然后基于这样的规则在代码中进行连接查询语句书写！
 *                 ②，同步:将常用的字段同步到一个库中进行联合查询！
 *                 ③，冗余:在一个库中冗余更多的连接查询需要的字段，保证全部数据都能查询到！
 *      2、唯一主键: 如果使用传统的自增等方式，多库中的主键id势必重复，所以需要对唯一性加以控制！
 *          解决方法:UUID(根据机器ID，时间等)，redis(单线程保证不重复)，snowflake算法（雪花）！
 *
 *      3、分布式事务:
 *          ~TCC:try控制业务代码流程，Confirm确认事务的正确性，cancel取消失败的事务！
 *
 *          ~基于消息系统的一致性方案:单节点事务完成后，通过发送消息保证事务提交，如果失败可通过重试，任务补偿等方式保证数据一致性！
 *
 *《再有人问你分布式事务，把这篇扔给他》
 *  - 事务的具体定义：事务提供一种机制将一个活动涉及的所有操作纳入到一个不可分割的执行单元，组成事务的所有操作只有在所有操作均能正常执行的情况下方能提交，只要其中任一操作执行失败，都将导致整个事务的回滚。
 *                  简单地说，事务提供一种“要么什么都不做，要么就都做”机制。
 *  一、数据库本地事务 - 数据库事务中的四大特性，ACID：
 *     1、ACID
 *       - A：原子性(Atomicity) - 一个事务(transaction)中的所有操作，要么全部完成，要么全部不完成，不会结束在中间某个环节。
 *       - C：一致性(Consistency) - 在一个事务执行之前和执行之后数据库都必须处于一致性状态。
 *       - I：隔离性(Isolation) - 指的是在并发环境中，当不同的事务同时操纵相同的数据时，每个事务都有各自的完整数据空间。
 *                               事务查看数据更新时，数据所处的状态要么是另一事务修改它之前的状态，要么是另一事务修改它之后的状态，事务不会查看到中间状态的数据。
 *       - D：持久性(Durability) - 指的是只要事务成功结束，它对数据库所做的更新就必须永久保存下来。
 *     2、InnoDB实现原理 - InnoDB是mysql的一个存储引擎，这里简单介绍一下数据库事务实现的一些基本原理，在本地事务中，服务和资源在事务的包裹下可以看做是一体的 - 我们的本地事务由资源管理器(RM)进行管理
 *        - 而事务的ACID是通过InnoDB日志和锁来保证。
 *          - 事务原子性和一致性通过Undo log来实现。- UndoLog的原理很简单，为了满足事务的原子性，在操作任何数据之前，首先将数据备份到一个地方（这个存储数据备份的地方称为UndoLog）。然后进行数据的修改。
 *                                                如果出现了错误或者用户执行了ROLLBACK语句，系统可以利用Undo Log中的备份将数据恢复到事务开始之前的状态。
 *          - 事务的隔离性是通过数据库锁机制实现的。
 *          - 事务的持久性通过redo log（重做日志）来实现。- 和Undo Log相反，RedoLog记录的是新数据的备份。在事务提交前，只要将RedoLog持久化即可，不需要将数据持久化。
 *                                                    当系统崩溃时，虽然数据没有持久化，但是RedoLog已经持久化。系统可以根据RedoLog的内容，将所有数据恢复到最新的状态。
 *  二、分布式事务 -
 *     1、什么是分布式事务 - 分布式事务就是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上。
 *                        简单的说，就是一次大的操作由不同的小操作组成，这些小的操作分布在不同的服务器上，且属于不同的应用，分布式事务需要保证这些小操作要么全部成功，要么全部失败。
 *                        本质上来说，分布式事务就是 为了保证不同数据库的数据一致性。
 *     2、分布式事务产生的原因 - 从上面本地事务来看，我们可以看为两块，一个是service产生多个节点，另一个是resource产生多个节点。
 *        - service多个节点：随着互联网快速发展，微服务，SOA等服务架构模式正在被大规模的使用，举个简单的例子，一个公司之内，用户的资产可能分为好多个部分，比如余额，积分，优惠券等等。
 *                          在公司内部有可能积分功能由一个微服务团队维护，优惠券又是另外的团队维护。这样的话就无法保证积分扣减了之后，优惠券能否扣减成功。
 *     3、分布式事务的基础 - 数据库的ACID四大特性，已经无法满足我们分布式事务，这个时候又有一些新的大佬提出一些新的理论：CAP、BASE
 *        3.1、CAP - CAP定理，又被叫作 布鲁尔定理。对于设计分布式系统来说(不仅仅是分布式事务)的架构师来说，CAP就是你的入门理论。
 *                - C (一致性)：对某个指定的客户端来说，读操作能返回最新的写操作。
 *                          对于数据分布在不同节点上的数据上来说，如果在某个节点更新了数据，那么在其他节点如果都能读取到这个最新的数据，那么就称为强一致，如果有某个节点没有读取到，那就是分布式不一致。
 *                - A (可用性)：非故障的节点在合理的时间内返回合理的响应(不是错误和超时的响应)。可用性的两个关键一个是合理的时间，一个是合理的响应。
 *                             合理的时间指的是请求不能无限被阻塞，应该在合理的时间给出返回。
 *                             合理的响应指的是系统应该明确返回结果并且结果是正确的，这里的正确指的是比如应该返回50，而不是返回40。
 *                - P (分区容错性)：当出现网络分区后，系统能够继续工作。打个比方，这里个集群有多台机器，有台机器网络出现了问题，但是这个集群仍然可以正常工作。
 *            - 熟悉CAP的人都知道,三者不能共有。在分布式系统中,网络无法100%可靠,分区其实是一个必然现象,如果我们选择了CA而放弃了P,那么当发生分区现象时,为了保证一致性,这个时候必须拒绝请求，但是A又不允许,
 *              所以分布式系统理论上不可能选择CA架构，只能选择 CP 或者 AP 架构。
 *            - 对于CP来说，放弃可用性，追求一致性和分区容错性，我们的zookeeper其实就是追求的强一致。
 *            - 对于AP来说，放弃一致性(这里说的一致性是强一致性)，追求分区容错性和可用性，这是很多分布式系统设计时的选择，后面的BASE也是根据AP来扩展。
 *        3.2、BASE - BASE 是 Basically Available(基本可用)、Soft state(软状态)和 Eventually consistent (最终一致性)三个短语的缩写。是对CAP中AP的一个扩展
 *                 - 1. 基本可用：分布式系统在出现故障时，允许损失部分可用功能，保证核心功能可用。
 *                 - 2. 软状态：允许系统中存在中间状态，这个状态不影响系统可用性，这里指的是CAP中的不一致。
 *                 - 3. 最终一致：最终一致是指经过一段时间后，所有节点数据都将会达到一致。
 *             - BASE解决了CAP中理论没有网络延迟，在BASE中用软状态和最终一致，保证了延迟后的一致性。
 *               BASE和 ACID 是相反的，它完全不同于ACID的强一致性模型，而是通过牺牲强一致性来获得可用性，并允许数据在一段时间内是不一致的，但最终达到一致状态。
 *  三、分布式事务解决方案 - 有了上面的理论基础后，这里介绍开始介绍几种常见的分布式事务的解决方案。
 *     - 是否真的要分布式事务：在说方案之前，首先你一定要明确你是否真的需要分布式事务？
 *                         - 上面说过出现分布式事务的两个原因，其中有个原因是因为 微服务过多。我见过太多团队一个人维护几个微服务，太多团队过度设计，搞得所有人疲劳不堪，
 *                           而微服务过多就会引出分布式事务，这个时候我不会建议你去采用下面任何一种方案，而是请把需要事务的微服务聚合成一个单机服务，使用数据库的本地事务。
 *                           因为不论任何一种方案都会增加你系统的复杂度，这样的成本实在是太高了，千万不要因为追求某些设计，而引入不必要的成本和复杂度。
 *     - 如果你确定需要引入分布式事务可以看看下面几种常见的方案。- 2PC、TCC、MQ事务、Saga事务
 *     1、2PC - 说到2PC就不得不聊数据库分布式事务中的 XA Transactions。
 *           - 在XA协议中分为两阶段：
 *              第一阶段：事务管理器要求每个涉及到事务的数据库预提交(precommit)此操作，并反映是否可以提交.
 *              第二阶段：事务协调器要求每个数据库提交数据，或者回滚数据。
 *           - 优点：尽量 保证了数据的强一致，实现成本较低，在各大主流数据库都有自己实现，对于MySQL是从5.5开始支持。
 *           - 缺点：1、单点问题：事务管理器在整个流程中扮演的角色很关键，如果其宕机，比如在第一阶段已经完成，在第二阶段正准备提交的时候事务管理器宕机，资源管理器就会一直阻塞，导致数据库无法使用。
 *                  2、同步阻塞：在准备就绪之后，资源管理器中的资源一直处于阻塞，直到提交完成，释放资源。
 *                  3、数据不一致：两阶段提交协议虽然为分布式数据强一致性所设计，但仍然存在数据不一致性的可能，比如在第二阶段中，假设协调者发出了事务commit的通知，
 *                    但是因为网络问题该通知仅被一部分参与者所收到并执行了commit操作，其余的参与者则因为没有收到通知一直处于阻塞状态，这时候就产生了数据的不一致性。
 *           - 总的来说，XA协议比较简单，成本较低，但是其单点问题，以及不能支持高并发(由于同步阻塞)依然是其最大的弱点。
 *     2、TCC -
 *        - TCC事务机制相比于上面介绍的XA，解决了其几个缺点：1. 解决了协调者单点，由主业务方发起并完成这个业务活动。业务活动管理器也变成多点，引入集群。
 *                                                    2. 同步阻塞：引入超时，超时后进行补偿，并且不会锁定整个资源，将资源转换为业务逻辑形式，粒度变小。
 *                                                    3. 数据一致性，有了补偿机制之后，由业务活动管理器控制一致性。
 *        - 对于TCC的解释：1、Try阶段：尝试执行,完成所有业务检查（一致性）,预留必须业务资源（准隔离性）
 *                       2、Confirm阶段：确认执行真正执行业务，不作任何业务检查，只使用Try阶段预留的业务资源，Confirm操作满足幂等性。要求具备幂等设计，Confirm失败后需要进行重试。
 *                       3、Cancel阶段：取消执行，释放Try阶段预留的业务资源 Cancel操作满足幂等性Cancel阶段的异常和Confirm阶段异常处理方案基本上一致。
 *        - 对于TCC来说适合一些：1、强隔离性，严格一致性要求的活动业务。
 *                            2、执行时间较短的业务
 *     3、MQ事务 - 在RocketMQ中实现了分布式事务，实际上其实是对 本地消息表 的一个封装，将本地消息表移动到了MQ内部。
 *              - 基本流程如下：1、第一阶段Prepared消息，会拿到消息的地址。
 *                            2、第二阶段执行本地事务。
 *                            3、第三阶段通过第一阶段拿到的地址去访问消息，并修改状态。消息接受者就能使用这个消息。
 *              - 如果消费超时，则需要一直重试，消息接收端需要保证幂等。如果消息消费失败，这个就需要人工进行处理，因为这个概率较低，如果为了这种小概率时间而设计这个复杂的流程反而得不偿失
 *     4、Saga事务 - 其核心思想是将长事务拆分为多个本地短事务，由Saga事务协调器协调，如果正常结束那就正常完成，如果某个步骤失败，则根据相反顺序一次 调用补偿操作。
 *                - Saga的组成：
 *  四、总结 - 还是那句话，能不用分布式事务就不用，如果非得使用的话，结合自己的业务分析，看看自己的业务比较适合哪一种，是在乎强一致，还是最终一致即可。
 *
 */
public class DistributedTransaction {
}
