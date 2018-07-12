package com.example.netty.netty_in_action.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by MintQ on 2018/7/12.
 *
 * 写一个 echo (回声) 客户端
 *  客户端要做的是：~连接服务器
 *               ~发送信息
 *               ~发送的每个信息，等待和接收从服务器返回的同样的信息
 *               ~关闭连接
 *
 *  1、用 ChannelHandler 实现客户端逻辑
 *      ~跟写服务器一样，我们提供 ChannelInboundHandler 来处理数据。
 *      ~下面例子，我们用 SimpleChannelInboundHandler 来处理所有的任务，需要覆盖三个方法：
 *          - channelActive() - 服务器的连接被建立后调用
 *          - channelRead0() - 数据从服务器接收到后调用
 *          - exceptionCaught() - 捕获一个异常时调用
 *
 */
@ChannelHandler.Sharable                                //1 - @Sharable标记这个类的实例可以在 channel 里共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2 - 当被通知该 channel 是活动的时候就发送信息
                CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //3 - 记录接收到的消息
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {                    //4 - 记录日志错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}
