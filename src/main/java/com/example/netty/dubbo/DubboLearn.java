package com.example.netty.dubbo;

/**
 * Created by MintQ on 2018/6/26.
 *
 * #分布式 | Dubbo 架构设计详解：
 *  ~Dubbo是Alibaba开源的分布式服务框架，它最大的特点是按照分层的方式来架构，使用这种方式可以使各个层之间解耦合（或者最大限度地松耦合）。
 *  ~从服务模型的角度来看，Dubbo采用的是一种非常简单的模型，要么是提供方提供服务，要么是消费方消费服务，所以基于这一点可以抽象出 服务提供方（Provider）和 服务消费方（Consumer）两个角色。
 *
 *
 *
 * 一、总体架构
 *    ~Dubbo框架设计一共划分了10个层，而最上面的Service层是留给实际想要使用Dubbo开发分布式服务的开发者实现业务逻辑的接口层。
 *    ~下面，结合Dubbo官方文档，我们分别理解一下框架分层架构中，各个层次的设计要点：
 *      1、服务接口层（Service）：该层是与实际业务逻辑相关的，根据 服务提供方 和 服务消费方 的业务设计对应的接口和实现。
 *      2、配置层（Config）：对外配置接口，以ServiceConfig和ReferenceConfig为中心，可以直接new配置类，也可以通过spring解析配置生成配置类。
 *      3、服务代理层（Proxy）：服务接口透明代理，生成服务的客户端Stub和服务器端Skeleton，以ServiceProxy为中心，扩展接口为ProxyFactory。
 *      4、服务注册层（Registry）：封装服务地址的注册与发现，以服务URL为中心，扩展接口为RegistryFactory、Registry和RegistryService。可能没有服务注册中心，此时服务提供方直接暴露服务。
 *      5、集群层（Cluster）：封装多个提供者的路由及负载均衡，并桥接注册中心，以Invoker为中心，扩展接口为Cluster、Directory、Router和LoadBalance。将多个服务提供方组合为一个服务提供方，实现对服务消费方来透明，只需要与一个服务提供方进行交互。
 *      6、监控层（Monitor）：RPC调用次数和调用时间监控，以Statistics为中心，扩展接口为MonitorFactory、Monitor和MonitorService。
 *      7、远程调用层（Protocol）：封将RPC调用，以Invocation和Result为中心，扩展接口为Protocol、Invoker和Exporter。Protocol是服务域，它是Invoker暴露和引用的主功能入口，它负责Invoker的生命周期管理。
 *      8、信息交换层（Exchange）：封装请求响应模式，同步转异步，以Request和Response为中心，扩展接口为Exchanger、ExchangeChannel、ExchangeClient和ExchangeServer。
 *      9、网络传输层（Transport）：抽象mina和netty为统一接口，以Message为中心，扩展接口为Channel、Transporter、Client、Server和Codec。
 *      10、数据序列化层（Serialize）：可复用的一些工具，扩展接口为Serialization、 ObjectInput、ObjectOutput和ThreadPool。
 *    ~根据官方提供的，对于上述各层之间关系的描述，如下所示：
 *      在RPC中，Protocol是核心层，也就是只要有Protocol + Invoker + Exporter就可以完成非透明的RPC调用，然后在Invoker的主过程上Filter拦截点。
 *      图中的Consumer和Provider是抽象概念，只是想让看图者更直观的了解哪些类分属于客户端与服务器端，不用Client和Server的原因是Dubbo在很多场景下都使用Provider、Consumer、Registry、Monitor划分逻辑拓普节点，保持统一概念。
 *      而Cluster是外围概念，所以Cluster的目的是将多个Invoker伪装成一个Invoker，这样其它人只要关注Protocol层Invoker即可，加上Cluster或者去掉Cluster对其它层都不会造成影响，因为只有一个提供者时，是不需要Cluster的。
 *      Proxy层封装了所有接口的透明化代理，而在其它层都以Invoker为中心，只有到了暴露给用户使用时，才用Proxy将Invoker转成接口，或将接口实现转成Invoker，也就是去掉Proxy层RPC是可以Run的，只是不那么透明，不那么看起来像调本地服务一样调远程服务。
 *      而Remoting实现是Dubbo协议的实现，如果你选择RMI协议，整个Remoting都不会用上，Remoting内部再划为Transport传输层和Exchange信息交换层，Transport层只负责单向消息传输，是对Mina、Netty、Grizzly的抽象，它也可以扩展UDP传输，而Exchange层是在传输层之上封装了Request-Response语义。
 *      Registry和Monitor实际上不算一层，而是一个独立的节点，只是为了全局概览，用层的方式画在一起。
 *
 * 二、Dubbo作为一个分布式服务框架，主要具有如下几个核心的要点：
 *    1、服务定义 :服务是围绕 服务提供方 和 服务消费方的，服务提供方实现服务，而服务消费方调用服务。
 *    2、服务注册：
 *      ~对于服务提供方，它需要发布服务，而且由于应用系统的复杂性，服务的数量、类型也不断膨胀；对于服务消费方，它最关心如何获取到它所需要的服务，而面对复杂的应用系统，需要管理大量的服务调用。而且，对于服务提供方和服务消费方来说，他们还有可能兼具这两种角色，即既需要提供服务，有需要消费服务。
 *      ~通过将服务统一管理起来，可以有效地优化内部应用对服务发布/使用的流程和管理。服务注册中心可以通过特定协议来完成服务对外的统一。
 *          Dubbo提供的注册中心有如下几种类型可供选择：1、Multicast注册中心 2、Zookeeper注册中心 3、Redis注册中心 4、Simple注册中心
 *    3、服务监控：
 *      ~无论是服务提供方，还是服务消费方，他们都需要对服务调用的实际状态进行有效的监控，从而改进服务质量。
 *    4、远程通信与信息交换
 *      ~远程通信需要指定通信双方所约定的协议，在保证通信双方理解协议语义的基础上，还要保证高效、稳定的消息传输。Dubbo继承了当前主流的网络通信框架，主要包括如下几个：Netty、Mina、Grizzly
 *    5、服务调用：
 *      ~dubbo的RPC调用模型分为 registry、provider、consumer、monitor 这几个部分。
 *      ~下面从Dubbo官网直接拿来，看一下基于RPC层，服务提供方和服务消费方之间的调用关系，上述图所描述的调用流程如下：
 *          1、服务提供方 发布服务 到服务注册中心；
 *          2、服务消费方从服务注册中心 订阅服务；
 *          3、服务消费方 调用 已经注册的可用服务
 *    6、注册/注销服务
 *      ~服务的注册与注销，是对服务提供方角色而言。
 *    7、服务订阅/取消
 *      ~为了满足应用系统的需求，服务消费方的可能需要从服务注册中心订阅指定的有服务提供方发布的服务，在得到通知可以使用服务时，就可以直接调用服务。反过来，如果不需要某一个服务了，可以取消该服务。
 *    8、协议支持
 *      ~Dubbo支持多种协议，如下所示：1、Dubbo协议 2、Hessian协议 3、HTTP协议 4、RMI协议 5、WebService协议 6、Thrift协议 7、Memcached协议 8、Redis协议
 *
 * 三、
 *
 * 《*》RPC框架的原理及实践：
 *    一、需求缘起：- 服务化的一个好处就是，不限定服务的提供方使用什么技术选型，能够实现大公司跨团队的技术解耦 。
 *               ~ 如果没有统一的服务框架， RPC 框架 ， 各个团队的服务提供方就需要各自实现一套序列化、反序列化、网络框架、连接池、收发线程、超时处理、状态机等“业务之外”的重复技术劳动 ，造成整体的低效。
 *                  所以， 统一 RPC 框架 把上述“业务之外”的技术劳动统一处理， 是服务化首要解决的问题 。
 *               ~
 *    二、 RPC 背景与过程：- 什么是 RPC （ Remote Procedure Call Protocol ），远程过程调用？
 *                      ~如果不使用RPC框架调用，不同的进程之间进行服务调用的话，就需要关注很多底层细节，如：（1）入参到字节流的转化，即序列化应用层协议细节
 *                                                                                              （2）socket 发送，即网络传输协议细节
 *                                                                                              （3）socket 接受
 *                                                                                              （4）字节流到出参的转化，即反序列化应用层协议细节
 *                      ~能不能调用层不关注这个细节呢？回答：可以，RPC 框架就是解决这个问题的，它能够让调用方“像调用本地函数一样调用远端的函数（服务）”
 *
 *    三、 RPC 框架职责 - 通过上面的讨论， RPC 框架要向调用方屏蔽各种复杂性，要向服务提供方也屏蔽各类复杂性 ：1、调用方感觉就像调用本地函数一样
 *                                                                                              2、服务提供方感觉就像实现一个本地函数一样来实现服务
 *                      ~再细化一些， client 端又包含：序列化、反序列化、连接池管理、负载均衡、故障转移、队列管理，超时管理、异步管理等职责。
 *                      ~server 端包含：服务端组件、服务端收发包队列、 io 线程、工作线程、序列化反序列化、上下文管理器、超时管理、异步回调等职责。
 *
 *    四、结论 ：1、 RPC 框架是微服务化架构的首要基础组件 ，它能大大降低微服务架构的成本，提高调用方与服务提供方的研发效率，屏蔽跨进程调用函数（服务）的各类复杂细节。
 *             2、RPC 框架的 职责 是： 让调用方感觉 就像调用本地函数一样 调用远端函数、让服务提供方感觉就像实现一个本地函数一样来实现服务
 *
 *
 */
public class DubboLearn {

}
