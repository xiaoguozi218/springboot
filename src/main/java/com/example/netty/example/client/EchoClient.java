package com.example.netty.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by MintQ on 2018/6/26.
 *
 * 服务器写好了，现在来写一个客户端连接服务器。应答程序的客户端包括以下几步：
     连接服务器
     写数据到服务器
     等待接受服务器返回相同的数据
     关闭连接
 *
 * 引导客户端:
     引导客户端启动和引导服务器很类似，客户端需同时指定host和port来告诉客户端连接哪个服务器。看下面代码：

    创建启动一个客户端包含下面几步：
     创建Bootstrap对象用来引导启动客户端
     创建EventLoopGroup对象并设置到Bootstrap中，EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
     创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
     添加一个ChannelHandler，客户端成功连接服务器后就会被执行
     调用Bootstrap.connect()来连接服务器
     最后关闭EventLoopGroup来释放资源
 *
 */
public class EchoClient {

    private final String host;
    private final int port;


    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient("127.0.0.1",8888).start();
    }
}
