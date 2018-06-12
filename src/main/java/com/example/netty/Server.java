package com.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by MintQ on 2018/6/12.
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(pGroup,cGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .option(ChannelOption.SO_SNDBUF,32*1024)
                .option(ChannelOption.SO_RCVBUF,32*1024)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });

        ChannelFuture channelFuture = b.bind(8888).sync();

        System.out.println("Server start...");

        channelFuture.channel().closeFuture().sync();

        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();
    }

}
