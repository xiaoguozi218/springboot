package com.example.netty.tcp_ip;
/**
 * @author  xiaoguozi
 * @create  2018/7/14 下午7:06
 * @desc    TCP/IP是个协议组
 *
 *《*》TCP/IP、HTTP、Socket 和 Servlet 之间的逻辑关系、系统点是怎样的？
 *  一、TCP、UDP和HTTP关系：
 *     1、TCP/IP是个协议组，可分为三个层次：网络层、传输层和应用层。
 *      - 在 网络层 有IP协议、ARP协议、ICMP协议、RARP协议和BOOTP协议。
 *      - 在 传输层 中有TCP协议与UDP协议。
 *      - 在 应用层 有FTP、HTTP、TELNET、SMTP、DNS等协议。
 *          因此，HTTP本身就是一个协议，是从Web服务器传输超文本到本地浏览器的传送协议。
 *
 *     2、HTTP协议是建立在 请求/响应 模型上的。
 *       - HTTP/1.0为每一次HTTP的请求/响应建立一条新的TCP链接，因此一个包含HTML内容和图片的页面将需要建立多次的短期的TCP链接。一次TCP链接的建立将需要3次握手。
 *
 *     3、结论：虽然HTTP本身是一个协议，但其最终还是基于TCP的。不过，目前，有人正在研究基于TCP+UDP混合的HTTP协议。
 *
 *  Socket是什么呢？
 *      - Socket是应用层与TCP/IP协议族通信的中间软件 抽象层，它是一组接口。
 *      - 在设计模式中，Socket其实就是一个 门面模式，它把复杂的TCP/IP协议族隐藏在Socket接口后面，对用户来说，一组简单的接口就是全部，让Socket去组织数据，以符合指定的协议。
 *
 *  二、socket和TCP/IP之间关系 - 实际上socket是对TCP/IP协议的封装，Socket本身并不是协议，而是一个 调用接口(API)。
 *     - TCP/IP协议是传输层协议，主要解决数据如何在网络中传输，而HTTP是应用层协议，主要解决如何包装数据。
 *
 *  三、servlet和HTTP之间的关系 - Servlet是J2EE的一个规范
 *     1、Servlet 是 J2EE 最重要的一部分，有了 Servlet 你就是 J2EE 了，J2EE 的其他方面的内容择需采用。
 *       而 Servlet 规范你需要掌握的就是 servlet 和 filter 这两项技术。绝大多数框架不是基于 servlet 就是基于 filter，如果它要在 Servlet 容器上运行，就永远也脱离不开这个模型。
 *     2、为什么 Servlet 规范会有两个包，javax.servlet 和 javax.servlet.http？
 *       - 早先设计该规范的人认为 Servlet 是一种服务模型，不一定是依赖某种网络协议之上，因此就抽象出了一个 javax.servlet ，
 *         同时再提供一个基于 HTTP 协议上的接口扩展。但是从实际运行这么多年来看，似乎没有发现有在其他协议上实现的 Servlet 技术。
 *     3、为什么我这么强调 HttpServletRequest 和 HttpServletResponse 这两个接口？
 *       - 因为 Web 开发是离不开 HTTP 协议的，而 Servlet 规范其实就是对 HTTP 协议做面向对象的封装，HTTP协议中的请求和响应就是对应了 HttpServletRequest 和 HttpServletResponse 这两个接口。
 *     4、你可以通过 HttpServletRequest 来获取所有请求相关的信息，包括 URI、Cookie、Header、请求参数等等，别无它路。因此当你使用某个框架时，你想获取HTTP请求的相关信息，只要拿到 HttpServletRequest 实例即可。
 *          而 HttpServletResponse接口是用来生产 HTTP 回应，包含 Cookie、Header 以及回应的内容等等。
 *     5、HTTP 协议里是没有关于 Session 会话的定义，Session 是各种编程语言根据 HTTP 协议的无状态这种特点而产生的。
 *        其实现无非就是服务器端的一个哈希表，哈希表的Key就是传递给浏览器的名为 jsessionid 的 Cookie 值。
 *
 *
 **/
public class TcpIpLearn {

}
