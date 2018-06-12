package com.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by MintQ on 2018/6/12.
 */
public class ClientHandler extends ChannelHandlerAdapter{

    public void chanelRead(ChannelHandlerContext ctx,Object msg)  {

        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);

            String body = new String(req,"utf-8");
            System.out.println("Client:"+body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        ctx.close();
    }
}
