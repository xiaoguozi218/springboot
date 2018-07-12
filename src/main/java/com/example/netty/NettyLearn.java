package com.example.netty;

/**
 * Created by MintQ on 2018/6/12.
 *
 *  Netty : 是一款基于NIO（Nonblocking I/O，非阻塞IO）开发的网络通信框架，对比于BIO（Blocking I/O，阻塞IO），它的并发性能得到了很大提高。
 *
 *  微服务将单体应用拆分为多个独立的微服务应用，每个应用独立运行，每个服务间通过远程调用（RPC）进行通信，此时高性能的通信方式就显得尤为重要，
 *  实现RPC通信的 底层框架Netty 由于其 稳定性、扩展性 以及框架成熟度的优秀表现，在RPC框架领域应用广泛，著名的 Hadoop、Dubbo、RocketMQ 等框架都使用了Netty作为其网络通信的底层框架。
 *
 *  为什么选择Netty?   Netty是基于Java NIO的网络应用框架，使用Netty可以快速开发网络应用，例如服务器和客户端的协议。Netty提供了一种新的方式来开发网络应用，使得它很容易使用和有很强的扩展性。
 *                    Netty内部的实现是复杂的，但是Netty提供了简单易用的API从网络处理代码中解耦业务逻辑。
 *
 *
 *  Netty的优点有：1、相较于JDK原生的NIO提供的API，Netty的API使用简单，开发门槛低；
 *               2、Netty修复了已经发现的JDK NIO BUG。 原生的 NIO 在 JDK 1.7 版本存在 epoll bug ：它会导致Selector空轮询，最终导致CPU 100%。官方声称在JDK 1.6版本的update18修复了该问题，但是直到JDK 1.7版本该问题仍旧存在，只不过该BUG发生概率降低了一些而已，它并没有得到根本性解决。
 *               3、功能强大，预置了多种编解码功能，支持多种协议开发；
 *               4、定制能力强，可以通过ChannelHandler进行扩展；
 *               5、性能高，对比其它NIO框架，Netty综合性能最优；
 *               6、经历了大规模的应用验证。在互联网、大数据、网络游戏、企业应用、电信软件得到成功，很多著名的框架通信底层就用了Netty，比如Dubbo；
 *
 *  Netty的使用：
 *      步骤：1．创建两个NioEventLoopGroup实例，NioEventLoopGroup是个线程组，它包含了一组NIO线程，专门用于网络事件处理。这里创建两个的原因是一个用于服务端接收客户端的连接，另一个用于进行SocketChannel的网络读写；
 *           2．创建一个ServerBootstrap对象，它是Netty用于启动NIO服务端的辅助启动类。配置Netty的一系列参数，例如将两个NIO线程组当作入参传递到ServerBootstrap中，设置创建的Channel，接受传出数据的缓存大小等；
 *           3．创建一个实际处理数据的类Channellnitalizer，绑定IO事件的处理类ChannelHandler，进行初始化的准备工作，比如设置接受传出数据的字符、格式以及实际处理数据的接口；
 *           4．绑定接口，执行同步阻塞方法；
 *           5．最后调用NIO线程组的shutdownGracefully进行优雅退出，它会释放跟shutdownGracefully相关联的资源。
 *
 *
 *  TCP拆包和粘包的问题解决：
 *      问题：在基于流的传输里比如TCP/IP，接收到的数据会先被存储到一个socket接收缓冲里。不幸的是，基于流的传输并不是一个数据包队列，而是一个字节队列。
 *           即使你发送了2个独立的数据包，操作系统也不会作为2个消息处理，而仅仅是作为一连串的字节而言。因此这是不能保证你远程写入的数据就会准确地读取。
 *           因此，一个接收方不管他是客户端还是服务端，都应该把接收到的数据整理成一个或者多个更有意义并且能够让程序的业务逻辑更好理解的数据。
 *
 *     解决方式：1.消息定长，例如每个报文的大小固定为200个字节，如果不够，空位补空格。
 *             2.在包尾部增加特殊字符进行分割，例如加”$”。
 *             3.将消息分为消息头和消息体，消息头中包含表示信息的总长度（或者消息体长度）的字段。
 *
 *
 *  用POJO代替ByteBuf ：在ChannelHandler中使用POJO的优点是显而易见的; 处理程序将从ByteBuf中提取信息的代码分离出来，使程序更易维护和可重用。
 *
 *
 *  Netty In Action 读书心得：
 *
 *  第一章：Netty介绍
 *
 *  第二章：第一个Netty程序
 *
 *  第三章：Netty核心概念
 *      在这一章我们将讨论Netty的 10 个核心类，清楚了解他们的结构对使用Netty很有用。可能有一些不会再工作中用到，但是也有一些很常用也很核心，你会遇到。
 *       Bootstrap or ServerBootstrap
         EventLoop
         EventLoopGroup
         ChannelPipeline
         Channel
         Future or ChannelFuture
         ChannelInitializer
         ChannelHandler
 *   3.1 Netty Crash Course
 *       一个Netty程序开始于 Bootstrap 类，Bootstrap类是Netty提供的一个可以通过简单配置来设置或"引导"程序的一个很重要的类。Netty中设计了 Handlers 来处理特定的"event"和设置Netty中的事件，从而来处理多个协议和数据。
 *      Netty连接客户端端或绑定服务器需要知道如何发送或接收消息，这是通过不同类型的handlers来做的，多个Handlers是怎么配置的？   Netty提供了 ChannelInitializer 类用来配置Handlers。
 *      Netty中所有的IO操作都是 异步执行 的，例如你连接一个主机默认是异步完成的；写入/发送消息也是同样是异步。
 *          也就是说操作不会直接执行，而是会等一会执行，因为你不知道返回的操作结果是成功还是失败，但是需要有检查是否成功的方法或者是注册监听来通知；Netty使用Futures和ChannelFutures来达到这种目的。Future注册一个监听，当操作成功或失败时会通知。ChannelFuture封装的是一个操作的相关信息，操作被执行时会立刻返回ChannelFuture。
 *   3.2 Channels,Events and Input/Output(IO)
 *      ~ Netty是一个非阻塞、事件驱动的网络框架。
 *      ~下图显示一个EventLoopGroup和一个Channel关联一个单一的EventLoop，Netty中的EventLoopGroup包含一个或多个EventLoop，而EventLoop就是一个Channel执行实际工作的线程。EventLoop总是绑定一个单一的线程，在其生命周期内不会改变。
 *      ~当注册一个Channel后，Netty将这个Channel绑定到一个EventLoop，在Channel的生命周期内总是被绑定到一个EventLoop。在Netty IO操作中，你的程序不需要同步，因为一个指定通道的所有IO始终由同一个线程来执行。
 *      ~
 *   3.3 什么是Bootstrap?为什么使用它？—— “引导”是Netty中配置程序的过程，当你需要连接客户端或服务器绑定指定端口时需要使用bootstrap。
 *      ~“引导”有两种类型：一种是用于客户端的Bootstrap(也适用于DatagramChannel)
 *                       一种是用于服务端的ServerBootstrap
 *      ~Bootstrap和ServerBootstrap之间的差异：
 *          Bootstrap用来连接远程主机，有1个EventLoopGroup
 *          ServerBootstrap用来绑定本地端口，有2个EventLoopGroup
 *      ~EventLoopGroup和EventLoop是什么关系？
 *          EventLoopGroup可以包含很多个EventLoop，每个Channel绑定一个EventLoop不会被改变，因为EventLoopGroup包含少量的EventLoop的Channels，很多Channel会共享同一个EventLoop。这意味着在一个Channel保持EventLoop繁忙会禁止其他Channel绑定到相同的EventLoop。
 *          我们可以理解为EventLoop是一个事件循环线程，而EventLoopGroup是一个事件循环集合。
 *      ~Netty允许处理IO和接受连接使用同一个EventLoopGroup，这在实际中适用于多种应用。
 *    3.4 Channel Handlers and Data Flow(通道处理和数据流)
 *      ~要明白Netty程序wirte或read时发生了什么，首先要对Handler是什么有一定的了解。
 *         在很多地方，Netty的ChannelHandler是你的应用程序中处理最多的。
 *         那么ChannelHandler究竟是什么？给ChannelHandler一个定义不容易，我们可以理解为ChannelHandler是一段执行业务逻辑处理数据的代码，它们来来往往的通过ChannelPipeline。
 *         实际上，ChannelHandler是定义一个handler的父接口，ChannelInboundHandler和ChannelOutboundHandler都实现ChannelHandler接口
 *
 *
 *
 * 《Netty 实战(精髓)》：是对 Norman Maurer 的 《Netty in Action》的一个精简
 *   一、Netty-异步和数据驱动
 *      ~什么是 Netty：Netty 是一个基于 NIO 的客户端、服务器端 编程框架。      - Netty 是一个广泛使用的 Java 网络编程框架。 - Netty是典型的Reactor模型结构
 *      ~一些历史：- 在网络发展初期，需要花很多时间来学习 socket 的复杂，寻址等等，在 C socket 库上进行编码，并需要在不同的操作系统上做不同的处理。
 *      ~JAVA NIO：- 在 2002 年，Java 1.4 引入了非阻塞 API 在 java.nio 包（NIO）。
 *              ~Blocking I/O:-当你的应用中连接数比较少，这个方案还是可以接受。当并发连接超过10000 时，context-switching（上下文切换）开销将是明显的。
 *      ~SELECTOR:-在这里，我们介绍了“Selector”，这是 Java 的非阻塞 I/O 实现的关键。
 *              ~Selector 最终决定哪一组注册的 socket 准备执行 I/O。正如我们之前所解释的那样，这 I/O 操作设置为非阻塞模式。通过通知，一个线程可以同时处理多个并发连接。（一个 Selector 由一个线程通常处理，但具体实施可以使用多个线程。）因此，每次读或写操作执行能立即检查完成。
 *              ~该模型提供了比 阻塞 I/O 模型 更好的资源使用，因为：- 可以用较少的线程处理更多连接，这意味着更少的开销在内存和上下文切换上
 *                                                           - 当没有 I/O 处理时，线程可以被重定向到其他任务上。
 *     1、Netty 介绍：- Netty 是一个广泛使用的 Java 网络编程框架。
 *
 *     2、构成部分：- Channel、Callback (回调)、Future、Event 和 Handler
 *          ~FUTURE, CALLBACK 和 HANDLER
 *              ~Netty 的 异步编程模型 是建立在 future 和 callback 的概念上的。
 *              ~一个 Netty 的设计的主要目标是促进“关注点分离”:你的 业务逻辑 从 网络基础设施应用程序中分离。
 *          ~SELECTOR, EVENT 和 EVENT LOOP
 *              ~Netty 通过触发事件从应用程序中抽象出 Selector，从而避免手写调度代码。EventLoop 分配给每个 Channel 来处理所有的事件，包括：
 *                  ~注册感兴趣的事件
 *                  ~调度事件到 ChannelHandler
 *                  ~安排进一步行动
 *              ~该 EventLoop 本身是由只有一个线程驱动，它给一个 Channel 处理所有的 I/O 事件，并且在 EventLoop 的生命周期内不会改变。
 *                  这个简单而强大的线程模型消除你可能对你的 ChannelHandler 同步的任何关注，这样你就可以专注于提供正确的回调逻辑来执行。
 *   二、第一个Netty应用
 *      ~总结：在本章中，您构建并运行你的第一 个Netty 的客户端和服务器。虽然这是一个简单的应用程序，它可以扩展到几千个并发连接。
 *
 *  三、Netty 总览
 *      ~本章主要了解 Netty 的架构模型，核心组件包括：
 *          - Bootstrap 和 ServerBootstrap
 *          - Channel
 *          - ChannelHandler
 *          - ChannelPipeline
 *          - ChannelFuture
 *          - EventLoop （事件循环）
 *      1、Netty 快速入门
 *          ~Bootstrap - Netty 应用程序通过设置 bootstrap（引导）类的开始，该类提供了一个 用于应用程序网络层配置的容器。
 *          ~Channel - 底层网络传输 API 必须提供给应用 I/O操作的接口，如读，写，连接，绑定等等。对于我们来说，这是结构几乎总是会成为一个“socket”。
 *                   - Netty 中的接口 Channel 定义了与 socket 丰富交互的操作集：bind, close, config, connect, isActive, isOpen, isWritable, read, write 等等。
 *          ~ChannelHandler - ChannelHandler 支持很多协议，并且提供用于 数据处理的容器。我们已经知道 ChannelHandler 由特定事件触发。
 *                          - ChannelHandler 可专用于几乎所有的动作，包括将一个对象转为字节（或相反），执行过程中抛出的异常处理。
 *                          - 常用的一个接口是 ChannelInboundHandler，这个类型接收到入站事件（包括接收到的数据）可以处理应用程序逻辑。当你需要提供响应时，你也可以从 ChannelInboundHandler 冲刷数据。
 *                            一句话，业务逻辑经常存活于一个或者多个 ChannelInboundHandler。
 *          ~ChannelPipeline - ChannelPipeline 提供了一个容器给 ChannelHandler 链并提供了一个API 用于管理沿着链入站和出站事件的流动。
 *                           - 每个 Channel 都有自己的ChannelPipeline，当 Channel 创建时自动创建的。
 *                           - ChannelHandler 是如何安装在 ChannelPipeline？- 主要是实现了ChannelHandler 的抽象 ChannelInitializer。ChannelInitializer子类 通过 ServerBootstrap 进行注册。当它的方法 initChannel() 被调用时，这个对象将安装自定义的 ChannelHandler 集到 pipeline。当这个操作完成时，ChannelInitializer 子类则 从 ChannelPipeline 自动删除自身。
 *          ~ChannelFuture - Netty 所有的 I/O 操作都是异步。因为一个操作可能无法立即返回，我们需要有一种方法在以后确定它的结果。
 *                         - 出于这个目的，Netty 提供了接口 ChannelFuture,它的 addListener 方法注册了一个 ChannelFutureListener ，当操作完成时，可以被通知（不管成功与否）。
 *          ~EventLoop - EventLoop 用于处理 Channel 的 I/O 操作。一个单一的 EventLoop通常会处理多个 Channel 事件。
 *                     - 一个 EventLoopGroup 可以含有多于一个的 EventLoop 和 提供了一种迭代用于检索清单中的下一个。
 *
 *      2、Channel, Event 和 I/O - Netty 是一个非阻塞、事件驱动的网络框架。
 *          ~Netty 实际上是使用 Threads（多线程）处理 I/O 事件，对于熟悉多线程编程的读者可能会需要关注 同步代码。这样的方式不好，因为同步会影响程序的性能，Netty 的设计保证程序处理事件不会有同步。
 *          ~这就是为什么你的应用程序 不需要同步 Netty 的 I/O操作 - 所有 Channel 的 I/O 始终用 相同的线程 来执行。
 *
 *      3、什么是 Bootstrapping 为什么要用 - 不管程序使用哪种协议，无论是创建一个客户端还是服务器都需要使用“引导”
 *          ~Bootstrapping（引导） 是 Netty 中配置程序的过程，当你需要连接客户端或服务器 绑定指定端口 时需要使用 Bootstrapping。
 *          ~Bootstrapping 有两种类型：- 一种是用于客户端的Bootstrap        - 用来连接远程主机，有1个EventLoopGroup
 *                                   - 一种是用于服务端的ServerBootstrap  - 用来绑定本地端口，有2个EventLoopGroup
 *      4、ChannelHandler 和 ChannelPipeline -关系： ChannelPipeline 是 ChannelHandler 链的容器。
 *          ~在许多方面的 ChannelHandler 是在您的应用程序的核心，尽管有时它 可能并不明显。ChannelHandler 支持广泛的用途，使它难以界定。因此，最好是把它当作一个通用的 容器，处理进来的事件（包括数据）并且通过ChannelPipeline。
 *          ~Netty 中有两个方向的数据流 - 入站(ChannelInboundHandler) 和 出站(ChannelOutboundHandler)
 *          ~当 ChannelHandler 被添加到的 ChannelPipeline 它得到一个 ChannelHandlerContext，它代表一个 ChannelHandler 和 ChannelPipeline 之间的“绑定”。它通常是安全保存对此对象的引用，除了当协议中的使用的是不面向连接（例如，UDP）。而该对象可以被用来获得 底层 Channel,它主要是用来写出站数据。
 *          ~还有，实际上，在 Netty 发送消息有两种方式。您可以直接写消息给 Channel 或写入 ChannelHandlerContext 对象。主要的区别是， 前一种方法会导致消息从 ChannelPipeline的尾部开始，而 后者导致消息从 ChannelPipeline 下一个处理器开始。
 *      5、近距离观察 ChannelHandler -
 *          下面解释下三个 ChannelHandler 的子类型：编码器、解码器以及 ChannelInboundHandlerAdapter 的子类 SimpleChannelInboundHandler
 *              ~编码器 - 如果该消息是出站会发生：“编码”，从一个Java对象转为字节。
 *              ~解码器 - 入站消息将从字节转为一个Java对象;也就是说，“解码”。
 *              ~SimpleChannelHandler
 *
 *
 *
 * 注意：
 *
 *
 */
public class NettyLearn {


    public static void main(String[] args) {

        System.err.println(Runtime.getRuntime().availableProcessors());
    }


}
