package com.example.netty.netty_in_action.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Auther: gsh
 * @Date: 2018/8/8 14:37
 * @Description: 采用Netty 实现 I/O 和 NIO - Blocking networking with Netty
 */
public class NettyBioServer {

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();        //1-创建一个 ServerBootstrap

            b.group(group)                                    //2-使用 OioEventLoopGroup 允许阻塞模式（Old-IO）
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//3-指定 ChannelInitializer 将给每个接受的连接调用
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {            //4-添加的 ChannelHandler 拦截事件，并允许他们作出反应
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);//5-写信息到客户端，并添加 ChannelFutureListener 当一旦消息写入就关闭连接
                                }
                            });
                        }
                    });
            ChannelFuture f = b.bind().sync();  //6-绑定服务器来接受连接
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();        //7-释放所有资源
        }
    }
}
