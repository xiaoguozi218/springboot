package com.example.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by MintQ on 2018/6/1.
 *
 * 这个非常精简的样例掀开了NIO多路复用的面纱，我们可以分析下主要步骤和元素：
 * 1、首先，通过Selector.open() 创建一个Selector,作为类似调度员的角色。
 *
 * 2、然后，创建一个ServerSocketChannel, 并且向Selector注册，通过指定SelectorKey.OP_ACCEPT,告诉调度员，它关注的是新的连接请求。
 *      注意：为什么我们要明确配置非阻塞模式呢？   这是因为阻塞模式下，注册操作是不允许的，会抛出 IIIegalBlockingModelException异常
 *
 * 3、Selector阻塞在Select操作，当有Channel发生接入请求，就会被唤醒。
 *
 * 4、在 sayHelloWorld 方法中，通过SocketChannel 和Buffer 进行数据操作
 *
 *
 *  总结：
 *  可以看到，在前面两个样例中，IO都是同步阻塞模式，所以需要多线程以实现多任务处理。
 *  而NIO则是利用了单线程轮询事件的机制，通过高效地定位就绪的Channel,来决定做什么，仅仅select阶段是阻塞的，可以有效避免大量客户端连接时，
 *  频繁线程切换带来的问题，应用的扩展能力有了非常大的提高。
 *
 */
public class NIOServer extends Thread{

    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(),8888));
            serverSocket.configureBlocking(false);
            //注册到Selector,并说明关注点
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select(); //阻塞等待就绪的 channel，这是关键点之一
                Set selectedKeys = selector.selectedKeys();
                Iterator iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    //生产系统中一般会额外进行就绪状态检查
                    sayHelloWorld((ServerSocketChannel) key.channel());
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sayHelloWorld(ServerSocketChannel server) {
        try {
            SocketChannel client = server.accept();
            client.write(Charset.defaultCharset().encode("Hello World!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NIOServer server = new NIOServer();
        server.start();
//        try {
//            Socket client = new Socket(InetAddress.getLocalHost(),server.getPort());
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            bufferedReader.lines().forEach(s -> System.out.println(s));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
