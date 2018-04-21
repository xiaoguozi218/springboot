package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gsh on 2018/4/21.
 *
 * 那也许你会问，如何使用Netty进行RPC服务器的开发呢？实际不难，下面我就简单的说明一下技术原理：

 　　1、定义RPC请求消息、应答消息结构，里面要包括RPC的接口定义模块、包括远程调用的类名、方法名称、参数结构、参数值等信息。

 　　2、服务端初始化的时候通过容器加载RPC接口定义和RPC接口实现类对象的映射关系，然后等待客户端发起调用请求。

 　　3、客户端发起的RPC消息里面包含，远程调用的类名、方法名称、参数结构、参数值等信息，通过网络，以字节流的方式送给RPC服务端，RPC服务端接收到字节流的请求之后，去对应的容器里面，查找客户端接口映射的具体实现对象。

 　　4、RPC服务端找到实现对象的参数信息，通过反射机制创建该对象的实例，并返回调用处理结果，最后封装成RPC应答消息通知到客户端。

 　　5、客户端通过网络，收到字节流形式的RPC应答消息，进行拆包、解析之后，显示远程调用结果。

 　　上面说的是很简单，但是实现的时候，我们还要考虑如下的问题：

 　　1、RPC服务器的传输层是基于TCP协议的，出现粘包咋办？这样客户端的请求，服务端不是会解析失败？好在Netty里面已经提供了解决TCP粘包问题的解码器：LengthFieldBasedFrameDecoder，可以靠它轻松搞定TCP粘包问题。

 　　2、Netty服务端的线程模型是单线程、多线程（一个线程负责客户端连接，连接成功之后，丢给后端IO的线程池处理）、还是主从模式（客户端连接、后端IO处理都是基于线程池的实现）。当然在这里，我出于性能考虑，使用了Netty主从线程池模型。

 　　3、Netty的IO处理线程池，如果遇到非常耗时的业务，出现阻塞了咋办？这样不是很容易把后端的NIO线程给挂死、阻塞？本文的处理方式是，对于复杂的后端业务，分派到专门的业务线程池里面，进行异步回调处理。

 　　4、RPC消息的传输是通过字节流在NIO的通道（Channel）之间传输，那具体如何实现呢？本文，是通过基于Java原生对象序列化机制的编码、解码器（ObjectEncoder、ObjectDecoder）进行实现的。当然出于性能考虑，这个可能不是最优的方案。更优的方案是把消息的编码、解码器，搞成可以配置实现的。具体比如可以通过：protobuf、JBoss Marshalling方式进行解码和编码，以提高网络消息的传输效率。

 　　5、RPC服务器要考虑多线程、高并发的使用场景，所以线程安全是必须的。此外尽量不要使用synchronized进行加锁，改用轻量级的ReentrantLock方式进行代码块的条件加锁。比如本文中的RPC消息处理回调，就有这方面的使用。

 　　6、RPC服务端的服务接口对象和服务接口实现对象要能轻易的配置，轻松进行加载、卸载。在这里，本文是通过Spring容器进行统一的对象管理。
 */
@RestController
public class NettyRpcController {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcController.class);



}
