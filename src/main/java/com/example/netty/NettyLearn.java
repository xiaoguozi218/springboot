package com.example.netty;

import io.netty.buffer.ByteBuf;

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
 *《Netty 实战(精髓)》：是对 Norman Maurer 的 《Netty in Action》的一个精简
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
 *          - ServerBootstrap：服务器端程序的入口,这是Netty为简化网络程序配置和关闭等生命周期管理,所引入的Bootstrapping机制.我们通常所做的创建Channel、绑定端口、注册Handler等,都可以通过这个统一的入口
 *            和 Bootstrap ：客户端的通常人口。
 *          - Channel：作为一个基于NIO的扩展框架，Channel和Selector等概念仍然是Netty的基础组件，但是针对应用开发具体需求，提供了相对易用的抽象。
 *          - ChannelHandler：这是应用开发者放置业务逻辑的主要地方，也是我上面提到的“Separation Of Concerns”原则的体现。
 *          - ChannelPipeline：它是ChannelHandler链条的容器，每个Channel在创建后，自动被分配一个ChannelPipeline。
 *          - ChannelFuture：这是Netty实现异步IO的基础之一，保证了同一个Channel操作的调用顺序。Netty扩展了Java标准的Future，提供了针对自己场景特有的Future定义。
 *          - EventLoop （事件循环）：这是Netty处理事件的核心机制。我们在NIO中通常要做的几件事情，如注册感兴趣的事件、调度相应的Handler等，都是EventLoop负责。
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
 *  四、核心功能
 *     1、Transport(传输) - 本章将涵盖很多 transport(传输)，他们的用例以及 API: - NIO、OIO(BIO)、Local(本地)、Embedded(内嵌)
 *         - Transport API: - Transport API 的核心是 Channel 接口.
 *           - Channel: 每个 Channel 都会分配一个 ChannelPipeline 和ChannelConfig。 - Channel 是线程安全(thread-safe)的
 *              - ChannelConfig: 负责设置并存储 Channel 的配置，并允许在运行期间更新它们。传输一般有特定的配置设置，可能实现了 ChannelConfig 的子类型。
 *              - ChannelPipeline: 容纳了使用的 ChannelHandler 实例，这些ChannelHandler 将处理通道传递的“入站”和“出站”数据以及事件。
 *                  - ChannelPipeline 实现了常用的 Intercepting Filter（拦截过滤器）设计模式。UNIX管道是另一例子：命令链接在一起，一个命令的输出连接到 的下一行中的输入。
*            - Channel 是线程安全(thread-safe)的，它可以被多个不同的线程安全的操作，在多线程环境下，所有的方法都是安全的。
 *             正因为 Channel 是安全的，我们存储对Channel的引用，并在学习的时候使用它写入数据到远程已连接的客户端，使用多线程也是如此。
 *         - Netty中的传输方式有如下 4 种：NIO、OIO、Local(本地)、Embedded(内嵌)
 *              1、NIO - io.netty.channel.socket.nio - 基于java.nio.channels的工具包，使用选择器作为基础的方法。
 *                  - NIO传输是目前最常用的方式，它通过使用选择器提供了完全异步的方式操作所有的 I/O，NIO 从Java 1.4才被提供。
 *              2、OIO - io.netty.channel.socket.oio - 基于java.net的工具包，使用阻塞流。
 *              3、Local - io.netty.channel.local - 用来在虚拟机之间本地通信。
 *              4、Embedded - io.netty.channel.embedded - 嵌入传输，它允许在没有真正网络的传输中使用 ChannelHandler，可以非常有用的来测试ChannelHandler的实现。
 *         - Transport 使用情况: - OIO-在低连接数、需要低延迟时、阻塞时使用
 *                              - NIO-在高连接数时使用
 *                              - Local-在同一个JVM内通信时使用
 *                              - Embedded-测试ChannelHandler时使用
 *     2、Buffer（缓冲）- Netty 中用 ByteBuf 替代 ByteBuffer，一个强大的实现，解决 JDK 的 API 的限制。
 *        - 正如我们先前所指出的，网络数据的基本单位永远是 byte(字节)。Java NIO 提供 ByteBuffer 作为字节的容器，但这个类是过于复杂，有点难以使用。
 *        2.1、Buffer API - ByteBuf、ByteBufHolder
 *             - Netty 使用 reference-counting(引用计数)来判断何时可以释放 ByteBuf 或 ByteBufHolder 和其他相关资源，从而可以利用池和其他技巧来提高性能和降低内存的消耗.
 *             - Netty Buffer API 提供了几个优势：可以自定义缓冲类型、通过一个内置的复合缓冲类型实现零拷贝、扩展性好，比如 StringBuilder、不需要调用 flip() 来切换读/写模式、
 *                                              读取和写入索引分开、方法链、引用计数、Pooling(池)
 *        2.2、ByteBuf - 字节数据的容器 - 所有的网络通信最终都是基于底层的 字节流传输，因此一个高效、方便、易用的数据接口是必要的，而 Netty 的 ByteBuf 满足这些需求。
 *             - ByteBuf 是一个很好的经过优化的数据容器，我们可以将字节数据有效的添加到 ByteBuf 中或从 ByteBuf 中获取数据。
 *             - ByteBuf 有2部分：一个用于读，一个用于写。
 *             1、ByteBuf 是如何工作的？-
 *                - 数据到 ByteBuf 后，writerIndex（写入索引）增加。开始读字节后，readerIndex（读取索引）增加。
 *                  你可以读取字节，直到写入索引和读取索引处在相同的位置，ByteBuf 变为不可读。当访问数据超过数组的最后位，则会抛出 IndexOutOfBoundsException。
 *                - ByteBuf 的默认最大容量限制是 Integer.MAX_VALUE。
 *                - ByteBuf类似于一个字节数组，最大的区别是读和写的索引可以用来控制对缓冲区数据的访问。
 *             2、ByteBuf 使用模式 - 1. HEAP BUFFER(堆缓冲区) 2.DIRECT BUFFER(直接缓冲区) 3.COMPOSITE BUFFER(复合缓冲区)
 *                - 1、堆缓冲区: 最常用的模式是 ByteBuf 将数据存储在 JVM 的堆空间，这是通过将数据存储在数组的实现。
 *                          堆缓冲区可以快速分配，当不使用时也可以快速释放。它还提供了直接访问数组的方法，通过 ByteBuf.array() 来获取 byte[]数据。
 *                        注意：访问非堆缓冲区 ByteBuf 的数组会导致UnsupportedOperationException， 可以使用 ByteBuf.hasArray()来检查是否支持访问数组。
 *                - 2、直接缓冲区：“直接缓冲区”是另一个 ByteBuf 模式。
 *                      - 在 JDK1.4 中被引入 NIO 的ByteBuffer 类允许 JVM 通过本地方法调用分配内存，其目的是：
 *                          1、通过免去中间交换的 内存拷贝, 提升IO处理速度; 直接缓冲区的内容可以驻留在垃圾回收扫描的堆区以外。
 *                          2、DirectBuffer 在 -XX:MaxDirectMemorySize=xxM大小限制下, 使用 Heap 之外的内存, GC对此”无能为力”,也就意味着规避了在高负载下频繁的GC过程对应用线程的中断影响
 *                      - 就解释了为什么“直接缓冲区”对于那些通过 socket 实现数据传输的应用来说，是一种非常理想的方式。
 *                        如果你的数据是存放在堆中分配的缓冲区，那么实际上，在通过 socket 发送数据之前，JVM 需要将先数据复制到直接缓冲区。
 *                      - 直接缓冲区的缺点：在内存空间的分配和释放上比堆缓冲区更复杂，另外一个缺点是如果要将数据传递给遗留代码处理，因为数据不是在堆上，你可能不得不作出一个副本。
 *                - 3、复合缓冲区 - 复合缓冲区就像一个列表，我们可以动态的添加和删除其中的 ByteBuf，JDK 的 ByteBuffer 没有这样的功能。
 *                      - Netty提供了 ByteBuf 的子类 CompositeByteBuf 类来处理复合缓冲区，CompositeByteBuf 只是一个视图。
 *        2.3、字节级别的操作 - 除了基本的读写操作， ByteBuf 还提供了它所包含的数据的修改方法。
 *        2.4、ByteBufHolder - Netty提供 ByteBufHolder 处理这种常见的情况。ByteBufHolder还提供对于Netty的高级功能,如缓冲池,其中保存实际数据的ByteBuf可以从池中借用,如果需要还可以自动释放。
 *        2.5、ByteBuf分配 -  ByteBuf 实例管理的几种方式：ByteBufAllocator、Unpooled（非池化）缓存、ByteBufUtil
 *             1、ByteBufAllocator - 为了减少分配和释放内存的开销，Netty 通过支持池类 ByteBufAllocator，可用于分配的任何 ByteBuf 我们已经描述过的类型的实例。
 *                      - 获得 ByteBufAllocator 的两种方式：1、从 channel 获得 ByteBufAllocator。 2、从ChannelHandlerContext 获得 ByteBufAllocator
 *             2、Unpooled - 当未引用ByteBufAllocator时,上面的方法无法访问到ByteBuf。对于这个用例 Netty 提供一个实用工具类称为 Unpooled,它提供了静态辅助方法来创建非池化的ByteBuf实例。
 *             3、ByteBufUtil - ByteBufUtil静态辅助方法来操作 ByteBuf，因为这个 API 是通用的，与使用池无关，这些方法已经在外面的分配类实现。
 *        2.6、引用计数器 - Netty4 引入了 引用计数器给 ByteBuf 和 ByteBufHolder（两者都实现了 ReferenceCounted 接口）。
 *             - 引用计数本身并不复杂;它在特定的对象上跟踪引用的数目。实现了ReferenceCounted 的类的实例会通常开始于一个活动的引用计数器为 1。活动的引用计数器大于0的对象被保证不被释放。
 *               当数量引用减少到0，该实例将被释放。
 *             - 技术就是诸如 PooledByteBufAllocator 这种减少内存分配开销的池化的精髓部分。
 *             - release（）将会递减对象引用的数目。当这个引用计数达到0时，对象已被释放，并且该方法返回 true。- 在一般情况下，最后访问的对象负责释放它。
 *     3、ChannelHandler 和 ChannelPipeline
 *        3.1、ChannelHandler 家族
 *             - Channel 生命周期：channelRegistered ——> channelActive ——> channelInactive ——> channelUnregistered
 *             - ChannelHandler 生命周期：当 ChannelHandler 添加到 ChannelPipeline，或者从 ChannelPipeline 移除后，这些将会调用。每个方法都会带 ChannelHandlerContext 参数
 *             - ChannelHandler 子接口：Netty 提供2个重要的 ChannelHandler 子接口：1、ChannelInboundHandler - 处理进站数据，并且所有状态都更改
 *                                                                             2、ChannelOutboundHandler - 处理出站数据，允许拦截各种操作
 *        3.2、ChannelPipeline
 *             - 每创建一个新的Channel ,就会分配一个新的 ChannelPipeline。这个关联是 永久性的;Channel 既不能附上另一个 ChannelPipeline 也不能分离 当前这个。
 *               这是一个 Netty 的固定方面的组件生命周期,开发人员无需特别处理。
 *             - 总结：- 一个 ChannelPipeline 是用来保存关联到一个 Channel 的ChannelHandler
 *                    - 可以修改 ChannelPipeline 通过动态添加和删除 ChannelHandler
 *                    - ChannelPipeline 有着丰富的API调用动作来回应入站和出站事件。
 *        3.3、ChannelHandlerContext - 一个接口
 *             - ChannelHandlerContext 代表 ChannelHandler 和ChannelPipeline 之间的关联,并在 ChannelHandler 添加到 ChannelPipeline 时创建一个实例。
 *               ChannelHandlerContext 的主要功能是管理通过同一个 ChannelPipeline 关联的 ChannelHandler 之间的交互。
 *             - 为什么共享 ChannelHandler？
 *               常见原因是要在多个 ChannelPipelines 上安装一个 ChannelHandler 以此来实现跨多个渠道收集统计数据的目的。
 *
 *        3.4、总结：本章带你深入窥探了一下 Netty 的数据处理组件: ChannelHandler。
 *               我们讨论了 ChannelHandler 之间是如何链接的以及它在像ChannelInboundHandler 和 ChannelOutboundHandler这样的化身中是如何与 ChannelPipeline 交互的。
 *
 *
 * 注意：1、Netty的“零拷贝” -
 *          - Zero-copy, 就是在操作数据时, 不需要将数据 buffer 从一个内存区域拷贝到另一个内存区域. 因为少了一次内存的拷贝, 因此 CPU 的效率就得到的提升。
 *          - 在 OS 层面上的 Zero-copy 通常指避免在 用户态(User-space) 与 内核态(Kernel-space) 之间来回拷贝数据。
 *          - 但Netty中的Zero-copy与 OS 的Zero-copy不太一样, Netty的 Zero-copy 完全是在用户态(Java 层面)的, 它的 Zero-copy 的更多的是偏向于 优化数据操作。
 *      2、Netty 的设计强调了 “Separation Of Concerns”，通过精巧设计的事件机制，将 业务逻辑 和 无关技术逻辑进行隔离，并通过各种方便的抽象，一定程度上填补了基础平台和业务开发之间的鸿沟，
 *         更有利于在应用开发中普及业界的最佳实践。   另外，Netty > java.nio+java.net ! - 从API能力范围来看，Netty完全是Java NIO框架的一个大大的超集。
 *
 */
public class NettyLearn {


    public static void main(String[] args) {

        System.err.println(Runtime.getRuntime().availableProcessors());
    }


}
