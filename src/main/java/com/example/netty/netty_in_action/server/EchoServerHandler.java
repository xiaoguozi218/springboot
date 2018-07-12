package com.example.netty.netty_in_action.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by MintQ on 2018/7/12.
 *
 * Netty 实现的 echo 服务器都需要下面这些：
 *  ~一个服务器 handler：这个组件实现了服务器的 业务逻辑，决定了连接创建后和接收到信息后该如何处理
 *  ~Bootstrapping： 这个是配置服务器的启动代码。最少需要设置服务器绑定的端口，用来监听连接请求。
 *
 * 1、通过 ChannelHandler 来实现服务器的逻辑
 *      ~关键点要牢记：
 *          ~ChannelHandler 是给 不同类型的事件调用
 *          ~应用程序实现或扩展 ChannelHandler 挂接到事件生命周期和提供自定义应用逻辑。
 * 2、引导服务器
 *      ~了解到业务核心处理逻辑 EchoServerHandler 后，下面要引导服务器自身了。
 *          ~监听和接收进来的 连接请求
 *          ~配置 Channel 来通知一个关于入站消息的 EchoServerHandler 实例
 *      ~Transport(传输）：在本节中，你会遇到“transport(传输）”一词。在网络的多层视图协议里面，传输层提供了用于端至端或主机到主机的通信服务。互联网通信的基础是 TCP 传输。
 *                        当我们使用术语“NIO transport”我们指的是一个传输的实现，它是大多等同于 TCP ，除了一些由 Java NIO 的实现提供了服务器端的性能增强。
 *
 */
@ChannelHandler.Sharable                                    //1 - @Sharable 标识这类的实例之间可以在 channel 里面共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));        //2 - 日志消息输出到控制台
        ctx.write(in);                            //3 - 将所接收的消息返回给发送者。注意，这还没有冲刷数据
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)        //4 - 冲刷所有待审消息到远程节点。关闭通道后，操作完成
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();                //5 - 打印异常堆栈跟踪
        ctx.close();                            //6 - 关闭通道
    }

}
