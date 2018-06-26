package com.example.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by MintQ on 2018/6/12.
 *
 * NIO (non-blocking io) : NIO也称为New IO，是一种 同步非阻塞 的通信模式，NIO 相对于BIO来说是一大进步。
 *                         客户端和服务器之间通过Channel通信，NIO可以在Channel进行读写操作，这些Channel都会被注册在Selector多路复用器上。
 *                         Selector通过一个线程不停的轮询这些Channel，找出已经准备就绪的Channel执行IO操作。
 *                         NIO 通过一个线程轮询，再基于轮询到的事件进行处理，不需要再为每个连接单独开个线程处理，从而以 较高的资源复用率 处理成千上万个客户端的请求，这就是非阻塞NIO的特点。
 *
 *  NIO的三大核心为 : Selector（选择器）: 是NIO编程的基础，非常重要，提供选择已经就绪的任务的能力。 当Channel注册到选择器后，Selector会分配给每个通道一个key值。Selector会不断地轮询注册在其上的Channel，如果某个Channel处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以取得就绪的Channel集合，从而进行后续的IO操作。
 *                  Buffer（缓冲区）: 它是NIO与BIO的一个重要区别。BIO是将数据直接写入或读取到Stream对象中，而NIO的数据操作都是在缓冲区进行的。缓冲区实际上也是一个数组，通常是一个字节数组（ByteBuffer），这个数组为缓冲区提供了数据的访问读写等操作属性，如位置、容量、上限等概念。（ByteBuffer由于只有一个位置指针处理读写操作，因此每次读写的时候都需要额外调用 flip() 将指针复位，否则功能将出错）
 *                  Channel（通道）: 和流不同，通道是 双向 的。通道分为两大类：一类是网络读写（SelectableChannel），我们使用的SocketChannel和ServerSocketChannel都是SelectableChannel的子类。
 *                                                                      一类是用于文件操作（FileChannel） 。
 *                                                                      最关键的是channel有多种状态位，可以与selector结合起来，方便selector去识别。
 *
 *
 *  注意：1、简单描述下工作原理。它使用了 reactor 模式: 一个线程通过多个任务之间的多路复用来满足所有请求，而从不在任何地方阻塞。
 *          只要有什么东西准备好了，它就会被这个线程(或者几个线程)处理。
 *      2、
 *
 *
 *
 */
public class Server implements Runnable{

    private Selector selector;
    //建立缓冲区
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);

    public Server(int port) {
        try {
            //打开选择器
            this.selector = Selector.open();
            //打开服务器通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(new InetSocketAddress(port));
            ssc.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("server start...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //循环遍历selector
        while (true) {
            try {
                //选择器开始监听
                selector.select();
                //返回结果集
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                //遍历Selection Keys
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    //判断是否有效
                    if (key.isValid()) {
                        //根据不同的事件状态，做不同操作
                        if (key.isAcceptable()) {
                            accept(key);
                        }
                        if (key.isReadable()) {
                            read(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void accept(SelectionKey key) {
        try {
            //获取服务通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            //执行阻塞方法
            SocketChannel sc = ssc.accept();
            //设置阻塞模式
            sc.configureBlocking(false);
            //注册到选择器上，并设置读取标识
            sc.register(this.selector,SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {

        try {
            readBuf.clear();
            SocketChannel sc = (SocketChannel) key.channel();
            int count = sc.read(readBuf);
            if (count == -1) {
                key.channel().close();
                key.cancel();
                return;
            }

            readBuf.flip();
            byte [] bytes = new byte[readBuf.remaining()];
            readBuf.get(bytes);
            String body = new String(bytes).trim();
            System.out.println("Server:"+body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Server(8888)).start();
    }
}
