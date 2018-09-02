package com.example.io;

/**
 * Created by gsh on 2018/7/19.
 *
 * 《IO模型及select、poll、epoll和kqueue的区别》
 *  一、五种IO模型：
 *     1、 BIO - blocking I/O            - 首先application调用 recvfrom()转入kernel，注意kernel有2个过程，wait for data和copy data from kernel to user。直到最后copy complete后，recvfrom()才返回。此过程一直是阻塞的。
 *     2、 NIO - nonblocking I/O         - 可以看见，如果直接操作它，那就是个 轮询。。直到内核缓冲区有数据。
 *     3、 多路复用IO - I/O multiplexing (select and poll) - 最常见的I/O复用模型 - select。- select先阻塞，有活动套接字才返回。与blocking I/O相比，select会有两次系统调用，但是select能处理多个套接字。
 *     4、 信号驱动IO - signal driven I/O (SIGIO) - 只有 UNIX 系统支持。与I/O multiplexing (select and poll)相比，它的优势是，免去了select的阻塞与轮询，当有活跃套接字时，由注册的handler处理。
 *     5、 AIO - asynchronous I/O  - 异步IO模型最大的特点是 完成后发回调通知。- 完全异步的I/O复用机制，因为纵观上面其它四种模型，至少都会在由kernel copy data to appliction时阻塞。而该模型是当copy完成后才通知application，可见是纯异步的。
 *                                                                        好像只有windows的完成端口是这个模型，效率也很出色。（windows的IOCP则是此模型）
 *
 *   - 异步非阻塞直接在完成后通知，用户进程只需要发起一个IO操作然后立即返回，等IO操作真正的完成以后，应用程序会得到IO操作完成的通知，
 *     此时用户进程只需要对数据进行处理就好了，不需要进行实际的IO读写操作，因为真正的IO读取或者写入操作已经由内核完成了。
 *
 *  2、 为什么epoll,kqueue 比select高级？
 *      - 答案是，他们无轮询。因为他们用callback取代了。
 *        想想看，当套接字比较多的时候，每次select()都要通过遍历FD_SETSIZE个Socket来完成调度,不管哪个Socket是活跃的,都遍历一遍。这会浪费很多CPU时间。
 *        如果能给套接字注册某个回调函数，当他们活跃时，自动完成相关操作，那就避免了轮询，这正是epoll与kqueue做的。
 *  3、 说到这里又得说说2个设计模式 - Reactor -
 *                            - Proactor -
 *  4、Java nio包是什么I/O机制？- 目前的java本质是select()模型，可以检查/jre/bin/nio.dll得知。至于java服务器为什么效率还不错。。我也不得而知，可能是设计得比较好吧。
 *
 * 二、epoll 与select的区别：
 *      1.select的句柄数目受限，在linux/posix_types.h头文件有这样的声明：#define __FD_SETSIZE  1024  表示select最多同时监听1024个fd。
 *                                                             而epoll没有，它的限制是最大的打开文件句柄数目。
 *      2.epoll的最大好处是不会随着FD的数目增长而降低效率，在selec中采用轮询处理，其中的数据结构类似一个数组的数据结构，而epoll不去轮询监听所有文件句柄是否已经就绪。
 *        epoll只对发生变化的文件句柄感兴趣。其工作机制是，使用"事件"的就绪通知方式，通过epoll_ctl注册文件描述符fd，一旦该fd就绪，内核就会采用类似callback的回调机制来激活该fd, epoll_wait便可以收到通知, 并通知应用程序。
 *      3.使用mmap加速内核与用户空间的消息传递。无论是select,poll还是epoll都需要内核把FD消息通知给用户空间，如何避免不必要的内存拷贝就很重要，在这点上，epoll是通过内核于用户空间mmap同一块内存实现的。
 *
 * - 关于epoll工作模式 - epoll有两种工作方式：ET，LT
 *      - ET：Edge Triggered，边缘触发。仅当状态发生变化时才会通知，epoll_wait返回。换句话，就是对于一个事件，只通知一次。且只支持 非阻塞的socket。
 *      - LT：Level Triggered，电平触发（默认工作方式）。类似select/poll,只要还有没有处理的事件就会一直通知，以LT方式调用epoll接口的时候，它就相当于一个速度比较快的poll.支持阻塞和不阻塞的socket。
 *
 *《深度理解select、poll和epoll，IO多路复用模型实现机制》：
 *  - 前言：在linux 没有实现epoll事件驱动机制之前，我们一般选择用select或者poll等IO多路复用的方法来实现并发服务程序。
 *         在大数据、高并发、集群等一些名词唱得火热之年代，select和poll的用武之地越来越有限，风头已经被epoll占尽。
 *  1、select()和poll() IO多路复用模型
 *    - select的缺点：
 *      1）单个进程能够监视的文件描述符的数量存在最大限制，通常是1024，当然可以更改数量，但由于select采用轮询的方式扫描文件描述符，文件描述符数量越多，性能越差；
 *         (在linux内核头文件中，有这样的定义：#define __FD_SETSIZE 1024)
 *      2）内核 / 用户空间内存拷贝问题，select需要复制大量的句柄数据结构，产生巨大的开销；
 *      3）select返回的是含有整个句柄的数组，应用程序需要遍历整个数组才能发现哪些句柄发生了事件；
 *      4）select的触发方式是水平触发，应用程序如果没有完成对一个已经就绪的文件描述符进行IO操作，那么之后每次select调用还是会将这些文件描述符通知进程。
 *    - 相比select模型，poll使用链表保存文件描述符，因此没有了监视文件数量的限制，但其他三个缺点依然存在。
 *    - 拿select模型为例，假设我们的服务器需要支持100万的并发连接，则在__FD_SETSIZE 为1024的情况下，则我们至少需要开辟1k个进程才能实现100万的并发连接。
 *      除了进程间上下文切换的时间消耗外，从内核/用户空间大量的无脑内存拷贝、数组轮询等，是系统难以承受的。
 *      因此，基于select模型的服务器程序，要达到10万级别的并发访问，是一个很难完成的任务。因此，该epoll上场了。
 *  2、epoll IO多路复用模型实现机制 - 由于epoll的实现机制与select/poll机制完全不同，上面所说的 select的缺点在epoll上不复存在。
 *    - 设想一下如下场景：有100万个客户端同时与一个服务器进程保持着TCP连接。而每一时刻，通常只有几百上千个TCP连接是活跃的(事实上大部分场景都是这种情况)。如何实现这样的高并发？
 *    - epoll的设计和实现与select完全不同。epoll通过在Linux内核中申请一个简易的文件系统(文件系统一般用什么数据结构实现？B+树)。
 *      把原先的select/poll调用分成了3个部分：
 *          1）调用epoll_create()建立一个epoll对象(在epoll文件系统中为这个句柄对象分配资源)
 *          2）调用epoll_ctl向epoll对象中添加这100万个连接的套接字
 *          3）调用epoll_wait收集发生事件的连接
 *    - 从上面的讲解可知：通过1、红黑树 和
 *                       2、双链表 数据结构，并结合
 *                       3、回调机制，造就了epoll的高效。
 *    - 讲解完了Epoll的机理，我们便能很容易掌握epoll的用法了。一句话描述就是：三步曲
 *          1）epoll_create()系统调用。此调用返回一个句柄，之后所有的使用都依靠这个句柄来标识。
 *          2）epoll_ctl()系统调用。通过此调用向epoll对象中添加、删除、修改感兴趣的事件，返回0标识成功，返回-1表示失败。
 *          3）epoll_wait()系统调用。通过此调用收集在epoll监控中已经发生的事件。
 *
 *《BIO和NIO的区别》：
 *      1、Java NIO和BIO之间第一个最大的区别是，BIO是 面向流 的，NIO是 面向缓冲区 的。
 *      2、BIO是阻塞式的、NIO是非阻塞的。
 *
 * 《为什么nio效率会比bio高？》
 *  1、同步阻塞IO（BIO）- 一个socket连接一个 处理线程（这个线程负责这个Socket连接的一系列数据传输操作）的。
 *                   - 所以这个阻塞的原因在在于的：操作系统允许的线程数量是有限的，多个socket申请与服务端建立连接时的，则服务端不能提供相应数量的处理线程的，没有分配到处理线程的连接就会阻塞等待或被拒绝的。
 *  2、同步非阻塞IO（NIO）- 这个New IO是对BIO的改进，这是基于 Reactor 模型的。
 *      - 基于事件驱动，当有连接请求，会将此连接注册到多路复用器上（selector）。
 *      - 在多路复用器上可以注册监听事件，比如监听可读、可写、可连接、可接受。
 *      - 通过监听，当真正有请求数据时，才来处理数据。
 *      - 不会阻塞，会不停的轮询是否有就绪的事件，所以处理顺序和连接请求先后顺序无关，与请求数据到来的先后顺序有关
 *
 *
 *《TCP/IP 协议组》 -
 *  1、对于 TCP 通信来说，每个TCP Socket在内核中都有一个发送缓冲区和一个接受缓冲区。- TCP的全双工的工作模式以及TCP的滑动窗口便依赖于这两个独立的Buffer及此Buffer的填充状态。
 *  2、而对于 UDP 通信来说，每个UDP Socket都有一个 接受缓冲区，而没有 发送缓冲区，从概念上来说就是只要有数据就发，不管对方是否可以正确接收，所以不缓冲，不需要发送缓冲区。
 *
 * - 总结一些重点：
 *    1、只有IOCP是asynchronous I/O，其他机制或多或少都会有一点阻塞。
 *    2、select低效是因为每次它都需要轮询。但低效也是相对的，视情况而定，也可通过良好的设计改善
 *    3、epoll, kqueue、select是Reacor模式，IOCP是Proactor模式。
 *    4、java nio包是select模型
 *
 *
 */
public class IOLearn {
}
