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
 * 《*》Java NIO详解：
 *  1、NIO的三大核心为 : Selector（选择器）: 是NIO编程的基础，非常重要，提供选择已经就绪的任务的能力。 当Channel注册到选择器后，Selector会分配给每个通道一个key值。Selector会不断地轮询注册在其上的Channel，如果某个Channel处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以取得就绪的Channel集合，从而进行后续的IO操作。
 *                  Buffer（缓冲区）: 它是NIO与BIO的一个重要区别。BIO是将数据直接写入或读取到Stream对象中，而NIO的数据操作都是在缓冲区进行的。缓冲区实际上也是一个数组，通常是一个字节数组（ByteBuffer），这个数组为缓冲区提供了数据的访问读写等操作属性，如位置、容量、上限等概念。（ByteBuffer由于只有一个位置指针处理读写操作，因此每次读写的时候都需要额外调用 flip() 将指针复位，否则功能将出错）
 *                  Channel（通道）: 和流不同，通道是 双向 的。通道分为两大类：一类是网络读写（SelectableChannel），我们使用的SocketChannel和ServerSocketChannel都是SelectableChannel的子类。
 *                                                                      一类是用于文件操作（FileChannel） 。
 *                                                                      最关键的是channel有多种状态位，可以与selector结合起来，方便selector去识别。
 *      1.1、Selector - Selector允许单线程处理多个 Channel
 *          (1) 为什么使用Selector?
 *              - 仅用单个线程来处理多个Channels的好处是，只需要更少的线程来处理通道。这样，可以减少线程上下文切换带来的开销，而且每个线程都要占用系统的一些资源（如内存）。因此，使用的线程越少越好。
 *              注意：但是，需要记住，现代的操作系统和CPU在多任务方面表现的越来越好，所以多线程的开销随着时间的推移，变得越来越小了。实际上，如果一个CPU有多个内核，不使用多任务可能是在浪费CPU能力。
 *                   不管怎么说，关于那种设计的讨论应该放在另一篇不同的文章中。在这里，只要知道使用Selector能够处理多个通道就足够了。
 *          (2) Selector的创建 - 通过调用Selector.open()方法创建一个Selector - Selector selector = Selector.open();
 *          (3) 向Selector注册通道 - 通过SelectableChannel.register()方法来实现
 *                               - 注意register()方法的第二个参数。这是一个“interest集合”，意思是在通过Selector监听Channel时对什么事件感兴趣。
 *                                 可以监听四种不同类型的事件： Connect、Accept、Read、Write
 *
 *      1.2、Channel - 通道 - 1、既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
 *                           2、通道可以异步地读写。
 *                           3、通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。
 *           - Channel的实现 ：1、FileChannel：从文件中读写数据。
 *                           2、DatagramChannel：能通过UDP读写网络中的数据。
 *                           3、SocketChannel：能通过TCP读写网络中的数据。
 *                           4、ServerSocketChannel：可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
 *
 *      1.3、Buffer - 缓冲区 - Java NIO中的Buffer用于和NIO的Channel(通道)进行交互。 如你所知，数据是从通道读入缓冲区，从缓冲区写入到通道中的。
 *           - 缓冲区本质上是一块可以写入数据，然后可以从中读取数据的 内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
 *           - Buffer的基本用法 :
 *              - 使用Buffer读写数据一般遵循以下四个步骤:1、写入数据到Buffer
 *                                                  2、调用flip()方法    - flip:快速翻转 - flip方法将Buffer从写模式切换到读模式。
 *                                                  3、从Buffer中读取数据
 *                                                  4、调用clear()方法或者compact()方法
 *              - 当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据。
 *              - 一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。
 *                clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
 *           - 为了理解Buffer的工作原理，需要熟悉它的三个属性：
 *              1、capacity - 作为一个内存块，Buffer有一个固定的大小值，也叫“capacity” - 不管Buffer处在什么模式，capacity的含义总是一样的。
 *              2、position -  - position和limit的含义取决于Buffer处在读模式还是写模式。
 *              3、limit  - 当切换Buffer到读模式时，limit会被设置成写模式下的position值。换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）
 *
 *           - Buffer的分配 - 要想获得一个Buffer对象首先要进行分配。 每一个Buffer类都有一个allocate方法。
 *                         - ByteBuffer buf = ByteBuffer.allocate(48);
 *
 *
 *  2、分散（Scatter）/聚集（Gather） - Java NIO开始支持scatter/gather，scatter/gather用于描述从Channel中读取或者写入到Channel的操作。
 *           - 分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。
 *           - 聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
 *
 *  3、
 *
 *
 *
 *
 *
 *  注意：1、简单描述下工作原理。它使用了 reactor 模式: 一个线程通过多个任务之间的多路复用来满足所有请求，而从不在任何地方阻塞。
 *          只要有什么东西准备好了，它就会被这个线程(或者几个线程)处理。
 *      2、NIO可让您只使用一个（或几个）单线程管理多个通道（网络连接或文件），但付出的代价是解析数据可能会比从一个阻塞流中读取数据更复杂。
 *      3、
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
