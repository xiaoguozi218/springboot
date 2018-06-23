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
 */
public class NettyLearn {


    public static void main(String[] args) {

        System.err.println(Runtime.getRuntime().availableProcessors());
    }


}
