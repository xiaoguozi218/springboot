package com.example.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by MintQ on 2018/6/12.
 */
public class Client {


    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1",8888);
        SocketChannel sc = null;
        ByteBuffer buf = ByteBuffer.allocate(1024);

        try {
            sc = SocketChannel.open();
            sc.connect(address);

            while (true) {
                byte[] bytes = new byte[1024];
                System.in.read(bytes);
                buf.put(bytes);
                buf.flip();
                sc.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
