package com.example.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by MintQ on 2018/6/12.
 *
 * BIO (blocking io): BIO即传统IO，是一种同步阻塞的通信模式，模式简单，使用方便。但并发处理能力低，通信耗时，依赖网速。应用程序在获取网络数据的时候，如果网络传输数据很慢，那么程序就一直等着，直到传输完毕为止。
 *
 *    采用BIO通信模型的服务端，通常由一个独立的Acceptor线程负责监听客户端的连接，它接收到客户端连接请求之后为每个客户端创建一个新的线程进行链路处理，
 *    每处理完成后，通过输出流返回应答给客户端，线程销毁。即典型的请求——应答通信模型。
 *
 *  这种处理方式在高并发的情况下，会创建出大量的线程，以至于最终耗尽资源，无法对外服务。
 *  所以传统的BIO线程模型，在现代情景的互联网应用中无法作为底层的通信模型进行使用。
 *
 */
public class Server {

    final static int PORT = 8888;

    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            //创建Server Socket
            //如果端口合法且空闲，服务端就监听成功
            server = new ServerSocket(PORT);
            System.out.println("server listening....");
            Socket socket = server.accept();
            //当有新的客户端接入时，会执行下面的代码
            new Thread(new ServerHandler(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }
    }
}
