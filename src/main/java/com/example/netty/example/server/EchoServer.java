package com.example.netty.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by MintQ on 2018/6/26.
 *
 * 通过创建ServerBootstrap对象来启动服务器，然后配置这个对象的相关选项，如端口、线程模式、事件循环，并且添加逻辑处理程序用来处理业务逻辑(下面是个简单的应答服务器例子)
 *
 * 本小节重点内容：
     创建ServerBootstrap实例来引导绑定和启动服务器
     创建NioEventLoopGroup对象来处理事件，如接受新连接、接收数据、写数据等等
     指定InetSocketAddress，服务器监听此端口
     设置childHandler执行所有的连接请求
     都设置完毕了，最后调用ServerBootstrap.bind() 方法来绑定服务器
 *
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        //create ServerBootstrap instance  

        try {
            ServerBootstrap b = new ServerBootstrap();
            //Specifies NIO transport, local socket address
            //Adds handler to channel pipeline  
            b.group(group).channel(NioServerSocketChannel.class).localAddress(port)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //Binds server, waits for server to close, and releases resources 
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName()+"started and listen on “"+f.channel().localAddress());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServer(8880).start();
    }
}
