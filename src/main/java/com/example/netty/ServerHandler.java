package com.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by MintQ on 2018/6/12.
 */
public class ServerHandler extends ChannelHandlerAdapter{

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        String body = new String(req,"utf-8");
        System.out.println("server:"+body);

        String response = "Hi Client!";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        ctx.close();
    }
}
