package com.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by MintQ on 2018/6/12.
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ClientHandler());
            }
        });

        ChannelFuture channelFuture = b.connect("127.0.0.1",8888).sync();

        System.out.println("Client start...");

        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty!".getBytes()));

        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
