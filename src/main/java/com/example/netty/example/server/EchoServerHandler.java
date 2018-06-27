package com.example.netty.example.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by MintQ on 2018/6/27.
 * 实现服务器业务逻辑
 *  Netty使用futures和回调概念，它的设计允许你处理不同的事件类型，更详细的介绍将再后面章节讲述，但是我们可以接收数据。你的channel handler必须继承ChannelInboundHandlerAdapter并且重写channelRead方法，这个方法在任何时候都会被调用来接收数据，在这个例子中接收的是字节。
 *
 *  下面是handler的实现，其实现的功能是将客户端发给服务器的数据返回给客户端：
 *
 *  Netty使用多个Channel Handler来达到对事件处理的分离，因为可以很容的添加、更新、删除业务逻辑处理handler。Handler很简单，它的每个方法都可以被重写，它的所有的方法中只有channelRead方法是必须要重写的。
 *
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx , Object msg) {
        System.out.println("Server received:"+msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
