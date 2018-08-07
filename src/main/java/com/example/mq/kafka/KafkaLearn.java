package com.example.mq.kafka;

/**
 * Created by gsh on 2018/6/20.
 *
 *  在流式计算中，Kafka一般用来缓存数据，Storm通过消费Kafka的数据进行计算。
 *  Kafka + Storm + Redis
 *
 *  (1)、Kafka是一个分布式消息队列：生产者、消费者的功能。它提供了类似于 JMS的特性，但是在设计实现上完全不同，此外它并不是JMS规范的实现。
 *  (2)、Kafka对消息保存时根据topic进行归类，发送消息称为Producer，消息接收者称为Consumer，此外Kafka集群有多个Kafka实例组成，每个实例（server）称为broker。
 *  (3)、无论是Kafka集群，还是Producer和Consumer都依赖于zookeeper集群保存一些meta信息，来保证系统可用性。
 *
 *
 *《简谈Kafka中的NIO网络通信模型》：1 + N + M
 *  一、 Kafka的网络通信模型是基于NIO的 Reactor 多线程模型来设计的。这里先引用Kafka源码中注释的一段话：An NIO socket server. The threading model is 1 Acceptor thread that handles new connections.
             Acceptor has N Processor threads that each have their own selector and read requests from sockets.
             M Handler threads that handle requests and produce responses back to the processor threads for writing.
 *  - Kafka的网络通信层模型，主要采用了1（1个Acceptor线程）+N（N个Processor线程）+M（M个业务处理线程）。
 *                              线程数	        线程名	            线程具体说明
                                 1	kafka-socket-acceptor_%x	Acceptor线程，负责监听Client端发起的请求
                                 N	kafka-network-thread_%d	    Processor线程，负责对Socket进行读写
                                 M	kafka-request-handler-_%d	Worker线程，处理具体的业务逻辑并生成Response返回
 *  二、 这里可以简单总结一下其网络通信模型中的几个重要概念：
 *    (1) Acceptor：1个接收线程，负责监听新的连接请求，同时注册OPACCEPT 事件，将新的连接按照"round robin"方式交给对应的 Processor 线程处理；
 *    (2) Processor：N个处理器线程，其中每个 Processor 都有自己的 selector，它会向 Acceptor 分配的 SocketChannel 注册相应的 OPREAD 事件，N 的大小由“num.networker.threads”决定；
 *    (3) KafkaRequestHandler：M个请求处理线程，包含在线程池—KafkaRequestHandlerPool内部，
 *                            从RequestChannel的全局请求队列—requestQueue中获取请求数据并交给KafkaApis处理，M的大小由“num.io.threads”决定；
 *    (4) RequestChannel：其为Kafka服务端的请求通道，该数据结构中包含了一个全局的请求队列 requestQueue和多个与Processor处理器相对应的响应队列responseQueue，
 *                        提供给Processor与请求处理线程KafkaRequestHandler和KafkaApis交换数据的地方；
 *    (5) NetworkClient：其底层是对 Java NIO 进行相应的封装，位于Kafka的网络接口层。Kafka消息生产者对象—KafkaProducer的send方法主要调用NetworkClient完成消息发送；
 *    (6) SocketServer：其是一个NIO的服务，它同时启动一个Acceptor接收线程和多个Processor处理器线程。提供了一种典型的Reactor多线程模式，将接收客户端请求和处理请求相分离；
 *    (7) KafkaServer：代表了一个Kafka Broker的实例；其startup方法为实例启动的入口；
 *    (8) KafkaApis：Kafka的业务逻辑处理Api，负责处理不同类型的请求；比如“发送消息”、“获取消息偏移量—offset”和“处理心跳请求”等；
 *
 *  1、SocketServer
 *      - SocketServer是接收 客户端Socket 请求连接、处理请求并返回处理结果的 核心类，Acceptor及Processor的初始化、处理逻辑都是在这里实现的。
 *
 *  2、Acceptor
 *      - Acceptor是一个继承自抽象类AbstractServerThread的线程类。
 *      - Acceptor的主要任务是监听并且接收客户端的请求，同时建立数据传输通道—SocketChannel，
 *        然后以轮询的方式交给一个后端的Processor线程处理（具体的方式是添加socketChannel至并发队列并唤醒Processor线程处理）。
 *  3、Processor
 *      - Processor同Acceptor一样，也是一个线程类，继承了抽象类AbstractServerThread。其主要是从客户端的请求中读取数据和将KafkaRequestHandler处理完响应结果返回给客户端。
 *  4、RequestChannel
 *      - 在Kafka的网络通信层中，RequestChannel为Processor处理器线程与KafkaRequestHandler线程之间的数据交换提供了一个数据缓冲区，是通信过程中Request和Response缓存的地方。
 *  5、KafkaRequestHandler
 *      - KafkaRequestHandler也是一种线程类，在KafkaServer实例启动时候会实例化一个线程池—KafkaRequestHandlerPool对象（包含了若干个KafkaRequestHandler线程），这些线程以守护线程的方式在后台运行。
 *  6、KafkaApis
 *      - KafkaApis是用于处理对通信网络传输过来的业务消息请求的中心转发组件。该组件反映出Kafka Broker Server可以提供哪些服务。
 *
 *《你必须要知道的kafka》 - Kafka社区非常活跃,从0.9 版本开始，Kafka的标语已经从“一个高吞吐量，分布式的消息系统”改为"一个分布式流平台"。
 *  1. 概述 -
 *      Kafka和传统的消息系统不同在于：1、kafka是一个分布式系统，易于向外扩展。
 *                                 2、它同时为发布和订阅 提供高吞吐量
 *                                 3、它支持多订阅者，当失败时能 自动平衡消费者
 *                                 4、消息的持久化
 *  2. 入门实例
 *  3. Kafka架构原理
 *      3.2 kafka名词解释 - 在一套kafka架构中有多个Producer，多个Broker,多个Consumer，每个Producer可以对应多个Topic，每个Consumer只能对应一个ConsumerGroup。
 *          - Partition：物理上的概念，一个topic可以分为多个partition，每个partition内部是有序的
 *          - offset：offset是一个long型的数字，我们通过这个offset可以确定一条在该partition下的唯一消息。在partition下面是保证了有序性，但是在topic下面没有保证有序性。
 *      3.5 网络模型 - 在kafka服务端采用的是 多线程 的Selector模型
 *      3.6 高可靠分布式存储模型 - 在Kafka中保证高可靠模型的依靠的是 副本机制，有了副本机制之后，就算机器宕机也不会发生数据丢失。
 *          3.6.1 高性能的日志存储
 *              - kafka一个topic下面的所有消息都是以partition的方式分布式的存储在多个节点上。每个Partition其实都会对应一个日志目录，在目录下面会对应多个日志分段(LogSegment)。
 *              - LogSegment文件由两部分组成：1、“.index”索引文件
 *                                         2、“.log”数据文件
 *              - 简单介绍一下如何读取数据：如果我们要读取第911条数据首先第一步，找到他是属于哪一段的，根据二分法查找到他属于的文件，找到0000900.index和00000900.log之后，然后去index中去查找 (911-900) =11这个索引或者小于11最近的索引,在这里通过二分法我们找到了索引是[10,1367]然后我们通过这条索引的物理位置1367，开始往后找，直到找到911条数据。
 *          3.6.2 副本机制 - Kafka的副本机制是多个服务端节点对其他节点的主题分区的日志进行复制。- 在Kafka中并不是所有的副本都能被拿来替代主副本
 *                          当集群中的某个节点出现故障，访问故障节点的请求会被转移到其他正常节点(这一过程通常叫Reblance)
 *                - 在kafka的leader节点中维护着一个ISR(In sync Replicas)集合，翻译过来也叫正在同步中集合，在这个集合中的需要满足两个条件：
 *                      1、节点必须和ZK保持连接
 *                      2、在同步的过程中这个副本不能落后主副本太多
 *  4. 高可用模型及幂等
 *      在分布式系统中一般有三种处理语义：
 *          1、at-least-once：至少一次，有可能会有多次。
 *          2、at-most-once：最多一次。- 如果在ack超时或返回错误时producer不重试
 *          3、exactly-once：刚好一次，即使producer重试发送消息，消息也会保证最多一次地传递给consumer。该语义是最理想的，也是最难实现的。
 *
 * 查漏补缺：
 *  1、segment达到一定的大小（可以通过配置文件设定,默认 1G ）后将不会再往该segment写数据，broker会创建新的segment。
 *  2、直击Kafka的心脏——控制器:  在Kafka集群中会有一个或者多个broker，其中有一个broker会被选举为控制器（Kafka Controller）:它负责管理整个集群中所有分区和副本的状态
 *      ~当某个分区的leader副本出现故障时，由控制器负责为该分区选举新的leader副本。当检测到某个分区的ISR集合发生变化时，由控制器负责通知所有broker更新其元数据信息。
 *       当使用kafka-topics.sh脚本为某个topic增加分区数量时，同样还是由控制器负责分区的重新分配。
 *      ~Kafka中的控制器选举的工作依赖于Zookeeper，成功竞选为控制器的broker会在Zookeeper中创建/controller这个临时（EPHEMERAL）节点，此临时节点的内容参考如下：{"version":1,"brokerid":0,"timestamp":"1529210278988"}
 *      ~在任意时刻，集群中有且仅有一个控制器。
 *      ~Zookeeper中还有一个与控制器有关的/controller_epoch节点，这个节点是持久（PERSISTENT）节点，节点中存放的是一个整型的controller_epoch值。controller_epoch用于记录控制器发生变更的次数，即记录当前的控制器是第几代控制器，我们也可以称之为“控制器的纪元”。controller_epoch的初始值为1，即集群中第一个控制器的纪元为1，当控制器发生变更时，没选出一个新的控制器就将该字段值加1。每个和控制器交互的请求都会携带上controller_epoch这个字段，如果请求的controller_epoch值小于内存中的controller_epoch值，则认为这个请求是向已经过期的控制器所发送的请求，那么这个请求会被认定为无效的请求。如果请求的controller_epoch值大于内存中的controller_epoch值，那么则说明已经有新的控制器当选了。由此可见，Kafka通过controller_epoch来保证控制器的唯一性，进而保证相关操作的一致性。
 *  3、在上图中在我们的生产者会决定发送到哪个Partition。
 *      - 1.如果没有Key值则进行轮询发送。
 *      - 2.如果有Key值，对Key值进行Hash，然后对分区数量取余，保证了同一个Key值的会被路由到同一个分区，如果想队列的强顺序一致性，可以让所有的消息都设置为同一个Key。
 *  4、在Kafka中并不是所有的副本都能被拿来替代主副本，所以在kafka的leader节点中维护着一个ISR(In sync Replicas)集合，翻译过来也叫正在同步中集合，在这个集合中的需要满足两个条件:
 *      - 1、节点必须和ZK保持连接
 *      - 2、在同步的过程中这个副本不能落后主副本太多
 *  5、当producer向leader发送数据时，可以通过request.required.acks参数来设置数据可靠性的级别：
 *      - 1（默认）：这意味着producer在ISR中的leader已成功收到的数据并得到确认后发送下一条message。如果leader宕机了，则会丢失数据。
 *      - 0：这意味着producer无需等待来自broker的确认而继续发送下一批消息。这种情况下数据传输效率最高，但是数据可靠性确是最低的。
 *      - -1：producer需要等待ISR中的所有follower都确认接收到数据后才算一次发送完成，可靠性最高。但是这样也不能保证数据不丢失，比如当ISR中只有leader时(其他节点都和zk断开连接，或者都没追上)，这样就变成了acks=1的情况。
 *
 *《kafka的一些常用命令》
 *  - 启动zookeeper ：bin/zookeeper-server-start.sh config/zookeeper.properties &
 *  - 启动kafka : bin/kafka-server-start.sh config/server.properties &
 *  - 停止kafka : bin/kafka-server-stop.sh
 *  - 停止zookeeper : bin/zookeeper-server-stop.sh
 *
 *
 */
public class KafkaLearn {

    public static void main(String[] args) {

        //producer
        new Thread(new UserKafkaProducer("test01")).start();

        //consumer
        new Thread(new KafkaConsumer()).start();
    }

}
