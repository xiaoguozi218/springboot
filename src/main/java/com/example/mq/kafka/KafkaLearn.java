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
 *
 * 查漏补缺：
 *  1、segment达到一定的大小（可以通过配置文件设定,默认 1G ）后将不会再往该segment写数据，broker会创建新的segment。
 *  2、直击Kafka的心脏——控制器:  在Kafka集群中会有一个或者多个broker，其中有一个broker会被选举为控制器（Kafka Controller）:它负责管理整个集群中所有分区和副本的状态
 *      ~当某个分区的leader副本出现故障时，由控制器负责为该分区选举新的leader副本。当检测到某个分区的ISR集合发生变化时，由控制器负责通知所有broker更新其元数据信息。
 *       当使用kafka-topics.sh脚本为某个topic增加分区数量时，同样还是由控制器负责分区的重新分配。
 *      ~Kafka中的控制器选举的工作依赖于Zookeeper，成功竞选为控制器的broker会在Zookeeper中创建/controller这个临时（EPHEMERAL）节点，此临时节点的内容参考如下：{"version":1,"brokerid":0,"timestamp":"1529210278988"}
 *      ~在任意时刻，集群中有且仅有一个控制器。
 *      ~Zookeeper中还有一个与控制器有关的/controller_epoch节点，这个节点是持久（PERSISTENT）节点，节点中存放的是一个整型的controller_epoch值。controller_epoch用于记录控制器发生变更的次数，即记录当前的控制器是第几代控制器，我们也可以称之为“控制器的纪元”。controller_epoch的初始值为1，即集群中第一个控制器的纪元为1，当控制器发生变更时，没选出一个新的控制器就将该字段值加1。每个和控制器交互的请求都会携带上controller_epoch这个字段，如果请求的controller_epoch值小于内存中的controller_epoch值，则认为这个请求是向已经过期的控制器所发送的请求，那么这个请求会被认定为无效的请求。如果请求的controller_epoch值大于内存中的controller_epoch值，那么则说明已经有新的控制器当选了。由此可见，Kafka通过controller_epoch来保证控制器的唯一性，进而保证相关操作的一致性。
 *
 *
 *
 *
 */
public class KafkaLearn {


}
