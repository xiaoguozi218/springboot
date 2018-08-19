package com.example.test.interview;

/**
 * Created by MintQ on 2018/7/10.
 *
 *《Spring》
 *    1、Spring 事务的隔离性，并说说每个隔离性的区别: 事务的隔离级别是为了解决并发问题。
 *       事务隔离级别: 1、读未提交 （会出现脏读, 不可重复读。基本不使用）
 *                   2、读已提交 （会出现不可重复读和幻读）               Oracle: 默认使用的是READ COMMITTED
 *                   3、可重复读 （会出现幻读）                         MYSQL: 默认为REPEATABLE_READ级别
 *                   4、可串行化读
 *       ~脏读 : 一个事务读取到另一事务 未提交 的更新数据
 *       ~不可重复读 : 在同一事务中, 多次读取同一数据返回的结果有所不同, 换句话说, 后续读取可以读到另一事务已提交的更新数据.
 *                      相反, "可重复读"在同一事务中多次读取数据时, 能够保证所读数据一样, 也就是后续读取不能读到另一事务已提交的更新数据
 *       ~幻读 : 一个事务读到另一个事务 已提交 的 insert数据
 *
 *    2、Spring事务的传播行为，并说说每个传播行为的区别: @Transactional(propagation=Propagation.REQUIRED)
 *      事物传播行为介绍:
 *          1、REQUIRED：     如果有事务, 那么加入事务, 没有的话新建一个(默认情况下)
 *          2、SUPPORTS：     如果有事务，就用，没有就不用。
 *          3、NOT_SUPPORTED：容器不为这个方法开启事务
 *          4、REQUIRES_NEW： 不管是否存在事务,都创建一个新的事务,原来的挂起,新的执行完毕,继续执行老的事务
 *          5、MANDATORY：    必须在一个已有的事务中执行,否则抛出异常
 *          6、NEVER：        必须在一个没有的事务中执行,否则抛出异常(与Propagation.MANDATORY相反)
 *
 *    3、你对spring的理解是什么? - 我们可以认为Spring是一个超级粘合平台
 *           1)开源框架
             2)IoC（控制反转）实现了低耦合 - 把Bean的 创建和注入 都交给Spring容器来管理，你只管开发和配置Bean即可。
             3)AOP (面向切面编程) 提高了复用性，将安全，事务等于程序逻辑相对独立的功能抽取出来，利用Spring的配置文件将这些功能插进去，实现了面向切面编程。
 *      IOC - Spring的最基本的功能就是创建对象及管理这些对象(Bean)之间的依赖关系，实现低耦合、高内聚。
 *      AOP - 还提供像通用日志记录、性能统计、安全控制、异常处理、数据库事务等面向切面的能力。
 *
 *   4、 Spring启动流程：- spring的启动过程其实就是其IoC容器的启动过程，对于web程序，IoC容器启动过程即是建立上下文的过程。
 *      1.首先，对于一个web应用，其部署在web容器中，web容器提供其一个全局的上下文环境，这个上下文就是 ServletContext，其为后面的spring IoC容器提供宿主环境；
 *      2.其次，在web.xml中会提供有contextLoaderListener。在web容器启动时，会触发容器初始化事件，此时 contextLoaderListener 会监听到这个事件，其 contextInitialized方法会被调用，
 *        在这个方法中，spring会初始化一个启动上下文，这个上下文被称为根上下文，即WebApplicationContext，这是一个接口类，确切的说，其实际的实现类是 XmlWebApplicationContext。
 *        这个就是spring的IoC容器，其对应的Bean定义的配置由web.xml中的 context-param标签指定。在这个IoC容器初始化完毕后，spring以WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE为属性Key，将其存储到ServletContext中，便于获取；
 *      3.再次，contextLoaderListener 监听器初始化完毕后，开始初始化web.xml中配置的Servlet，这里是 DispatcherServlet，这个servlet实际上是一个标准的前端控制器，用以转发、匹配、处理每个servlet请 求。
 *        DispatcherServlet上下文在初始化的时候会建立自己的IoC上下文，用以持有spring mvc相关的bean。
 *
 *   5、Spring 中有多少种 IOC 容器？ BeanFactory 、ApplicationContext
 *      - BeanFactory ： BeanFactory 就像一个包含 bean 集合的工厂类。它会在客户端要求时实例化 bean。
 *      - ApplicationContext ： ApplicationContext 接口扩展了 BeanFactory 接口。它在 BeanFactory 基础上提供了一些额外的功能。
 *
 *   6、Spring IoC 的实现机制 ：Spring 中的 IoC 的实现原理就是 工厂模式 加 反射机制。
 *
 *   7、Spring 支持几种 bean scope？ Spring bean 支持 5 种 scope：
 *      1、Singleton - 每个 Spring IoC 容器仅有一个单实例。
 *      2、Prototype - 每次请求都会产生一个新的实例。
 *      3、Request - 每一次 HTTP 请求都会产生一个新的实例，并且该 bean 仅在当前 HTTP 请求内有效。
 *      4、Session - 每一次 HTTP 请求都会产生一个新的 bean，同时该 bean 仅在当前 HTTP session 内有效。
 *      5、Global-session - 类似于标准的 HTTP Session 作用域，不过它仅仅在基于 portlet 的 web 应用中才有意义。Portlet 规范定义了全局 Session 的概念，它被所有构成某个 portlet web 应用的各种不同的 portlet 所共享。
 *   8、Spring自动装配有哪些方式？ no、byName 、byType 、构造函数、autodetect 。
 *
 *   9、@Autowired 注解有什么用？- @Autowired 可以更准确地控制应该在何处以及如何进行自动装配。此注解用于在 setter 方法，构造函数，具有任意名称或多个参数的属性或方法上自动装配 bean。默认情况下，它是类型驱动的注入。
 *
 *   10、Spring AOP and AspectJ AOP 有什么区别？
 *      - Spring AOP 基于动态代理方式实现；AspectJ 基于静态代理方式实现。
 *
 *《MyBatis》
 *   1、MyBatis中#{}与${}的区别：
 *      - #方式能够很大程度防止sql注入 - #{} 在预处理时，会把参数部分用一个占位符 ? 代替 - #{} 的参数替换是发生在 DBMS 中，而 ${} 则发生在动态解析过程中。
 *      - $方式无法防止Sql注入
 *      - 一般能用#的就别用$
 *      - MyBatis排序时使用order by 动态参数时需要注意，用$而不是#
 *
 *
 *
 * 《*》负载均衡、集群相关
 *     1、Nginx+Tomcat+Redis实现负载均衡、资源分离、session共享
 *
 *     2、峰值QPS和机器计算公式：原理：每天80%的访问集中在20%的时间里，这20%时间叫做峰值时间
 *        - 公式：( 总PV数 * 80% ) / ( 每天秒数 * 20% ) = 峰值时间每秒请求数(QPS)
 *        - 机器：峰值时间每秒QPS / 单台机器的QPS = 需要的机器
 *       问：每天300w PV 的在单台机器上，这台机器需要多少QPS？ -  答：( 3000000 * 0.8 ) / (86400 * 0.2 ) = 139 (QPS)
 *
 *       问：如果一台机器的QPS是58，需要几台机器来支持？- 答：139 / 58 = 3
 *
 * 《*》多线程、并发及线程的基础问题：
 *     1、volatile 能使得一个非原子操作变成原子操作吗？
 *          一个典型的例子是在类中有一个 long 类型的成员变量。如果你知道该成员变量会被多个线程访问，如计数器、价格等，你最好是将其设置为 volatile。
 *          为什么？因为 Java 中读取 long 类型变量不是原子的，需要分成两步，如果一个线程正在修改该 long 变量的值，另一个线程可能只能看到该值的一半（前 32 位）。但是对一个 volatile 型的 long 或 double 变量的读写是原子。（？）
 *     2、volatile 修饰符的有过什么实践？
 *          ~一种实践是用 volatile 修饰 long 和 double 变量，使其能按原子类型来读写。double 和 long 都是64位宽，因此对这两种类型的读是分为两部分的，第一次读取第一个 32 位，然后再读剩下的 32 位，这个过程不是原子的，
 *           但 Java 中 volatile 型的 long 或 double 变量的读写是原子的。
 *          ~volatile 修复符的另一个作用是提供内存屏障（memory barrier），例如在分布式框架中的应用。简单的说，就是当你写一个 volatile 变量之前，Java 内存模型会插入一个写屏障（write barrier），读一个 volatile 变量之前，会插入一个读屏障（read barrier）。
 *           意思就是说，在你写一个 volatile 域时，能保证任何线程都能看到你写的值，同时，在写之前，也能保证任何数值的更新对所有线程是可见的，因为内存屏障会将其他所有写的值更新到缓存。
 *     3、volatile 类型变量提供什么保证？ 保证 可见性 和 有序性
 *          ~例如，JVM 或者 JIT为了获得更好的性能会对语句重排序，但是 volatile 类型变量即使在没有同步块的情况下赋值也不会与其他语句重排序。 volatile 提供 happens-before 的保证，确保一个线程的修改能对其他线程是可见的。
 *          ~某些情况下，volatile 还能提供原子性，如读 64 位数据类型，像 long 和 double 都不是原子的，但 volatile 类型的 double 和 long 就是原子的。
 *
 *     4、你是如何调用 wait（）方法的？使用 if 块还是循环？为什么？
 *          ~wait() 方法应该在 循环 调用，因为当线程获取到 CPU 开始执行的时候，其他条件可能还没有满足，所以在处理前，循环检测条件是否满足会更好。
 *     5、什么是线程局部变量？ - ThreadLocal
 *          ~线程局部变量是局限于线程内部的变量，属于线程自身所有，不在多个线程间共享。Java 提供 ThreadLocal 类来支持 线程局部变量，是一种 实现线程安全 的方式。
 *          ~但是在管理环境下（如 web 服务器）使用线程局部变量的时候要特别小心，在这种情况下，工作线程的生命周期比任何应用变量的生命周期都要长。任何线程局部变量一旦在工作完成后没有释放，Java 应用就存在内存泄露的风险。
 *          - 数据结构：
 *     6、Java 中 sleep 方法和 wait 方法的区别？
 *          ~虽然两者都是用来暂停当前运行的线程，但是~ sleep() 实际上只是短暂停顿，因为它 不会释放锁
 *                                             ~而wait() 意味着条件等待，这就是为什么该方法要 释放锁，因为只有这样，其他等待的线程才能在满足条件时获取到该锁。
 *     7、什么是不可变对象（immutable object）？Java 中怎么创建一个不可变对象？
 *          ~不可变对象指对象一旦被创建，状态就不能再改变。任何修改都会创建一个新的对象，如 String、Integer及其它包装类。
 *
 *《计算机网络》-
 *  1、为什么有了MAC(IP)还需要IP(MAC)？ （MAC地址已经是唯一了，为什么需要IP地址？）
 *     - MAC是链路层，IP是网络层，每一层干每一层的事儿，之所以在网络上分链路层、网络层(...，就是将问题简单化。
 *     - 历史的兼容问题。
 *  2、TCP 每个状态说一下，TIME-WAIT状态说一下？
 *     1、CLOSED：初始状态，表示TCP连接是“关闭着的”或“未打开的”。
 *     2、LISTEN：表示服务器端的某个SOCKET处于监听状态，可以接受客户端的连接。
 *     3、SYN-SENT：表示客户端已发送SYN报文。当客户端SOCKET执行connect()进行连接时，它首先发送SYN报文，然后随即进入到SYN_SENT状态。
 *     4、SYN_RCVD：表示服务器接收到了来自客户端请求连接的SYN报文。当TCP连接处于此状态时，再收到客户端的ACK报文，它就会进入到ESTABLISHED状态。
 *     5、ESTABLISHED：表示TCP连接已经成功建立。
 *     6、FIN-WAIT-1：第一次主动请求关闭连接,等待对方的ACK响应。
 *     7、CLOSE_WAIT：对方发了一个FIN报文给自己，回应一个ACK报文给对方。此时进入CLOSE_WAIT状态。
 *        - 接下来呢，你需要检查自己是否还有数据要发送给对方，如果没有的话，那你也就可以close()这个SOCKET并发送FIN报文给对方，即关闭自己到对方这个方向的连接
 *     8、FIN-WAIT-2：主动关闭端接到ACK后，就进入了FIN-WAIT-2。在这个状态下，应用程序还有接受数据的能力，但是已经无法发送数据。
 *     9、LAST_ACK：当被动关闭的一方在发送FIN报文后，等待对方的ACK报文的时候，就处于LAST_ACK 状态
 *     10、CLOSED：当收到对方的ACK报文后，也就可以进入到CLOSED状态了。
 *     11、TIME_WAIT：表示收到了对方的FIN报文，并发送出了ACK报文。TIME_WAIT状态下的TCP连接会等待2*MSL
 *         - （Max Segment Lifetime，最大分段生存期，指一个TCP报文在Internet上的最长生存时间。）
 *     12、CLOSING：罕见的状态。表示双方都正在关闭SOCKET连接
 *
 *《操作系统》-
 *
 *
 *《Java核心技术36讲 总结》： https://time.geekbang.org/column/82
 *  1、列几个finally 不会被执行的情况：
 *      - 1、try-catch异常退出。比如：在try中 执行 System.exit(1);
 *      - 2、无限循环。 try中 执行  while (true)
 *      - 3、线程被杀死。 当执行try,finally的线程被杀死时，finally也无法执行。
 *    总结：1、不要再finally中使用return语句。
 *          2、finally总是执行，除非程序或者线程被中断。
 *  2、
 *
 */
public class InterviewQuestions {

    public static void main(String[] args) {
        Integer t1 = 1;
        Integer t2 = 1;
        System.out.println(t1 == t2);   //true
    }

}
