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
 */
public class KafkaLearn {


}
