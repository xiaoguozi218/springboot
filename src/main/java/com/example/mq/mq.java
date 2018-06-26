package com.example.mq;

/**
 * Created by MintQ on 2018/5/30.
 *  消息队列 （Message Queue）        消息系统的核心作用就是三点：解耦，异步和并行
 *      1、解耦合
 *      2、提高系统的响应时间
 *
 *  消息队列分类： 1、点对点：消息生产者生产消息发送到queue中，然后 消息消费者从queue中取出并且消费消息。
 *               2、发布/订阅：消息生产者（发布）将消息发布到topic中，同时有多个消息消费者（订阅）消费该消息。和点对点方式不同，发布到topic的消息会被所有订阅者消费。
 *
 *  Kafka 简介：Kafka是分布式发布-订阅的消息系统。它最初由Linkedln公司开发，使用Scala语言编写，之后成为Apache项目的一部分。
 *
 *  Kafka的特点：1、高吞吐量。
 *              2、可进行持久化操作。
 *              3、分布式系统，易于向外扩展。 所有的producer、broker和consumer都会有多个，均为分布式的。无需停机即可扩展机器。
 *              4、消息被处理的状态是在consumer端维护，而不是由server端维护。 当失败时能自动平衡。  （broker直管存消息和删消息，它是不维护消息状态的，它是无状态的！！）
 *              5、支持online和offline的场景。
 *
 *  Kafka的核心概念：1、Producer 特指消息的生产者
 *                 2、Consumer 特指消息的消费者
 *                 3、Consumer Group 消费者组，可以并行消费Topic中partition的消息
 *                 4、Broker ：缓存代理，Kafka集群中的一台或多台服务器统称为broker 。
 *                 5、topic : 特指Kafka处理的消息源（feeds of messages）的不同分类。
 *                 6、Partition ：topic物理上的分组，一个topic可以分为多个partition，每个partition是一个有序的队列。partition中的每条消息都会被分配一个有序的id（offset）。
 *
 *                 7、Message ：消息，是通信的基本单位，每个producer可以向一个topic发布一些消息。
 *
 *
 *
 *
 *
 *
 */
public class mq {


}
