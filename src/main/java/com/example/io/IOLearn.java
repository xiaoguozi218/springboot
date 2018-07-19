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
