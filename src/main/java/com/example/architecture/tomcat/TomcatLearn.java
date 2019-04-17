package com.example.architecture.tomcat;

/**
 *
 * Tomcat 设计模式分析 - 门面设计模式、观察者设计模式、命令设计模式、责任链模式、
 *  1、门面设计模式
 *    - 门面设计模式在 Tomcat 中有多处使用，在 Request 和 Response 对象封装中、Standard Wrapper 到 ServletConfig 封装中、ApplicationContext 到 ServletContext 封装中等都用到了这种设计模式。
 *    - 门面设计模式的原理： 顾名思义，就是将一个东西封装成一个门面好与人家更容易进行交流，就像一个国家的外交部一样。
 *          这种设计模式主要用在一个大的系统中有多个子系统组成时，这多个子系统肯定要涉及到相互通信，但是每个子系统又不能将自己的内部数据过多的暴露给其它系统，不然就没有必要划分子系统了。
 *      每个子系统都会设计一个门面，把别的系统感兴趣的数据封装起来，通过这个门面来进行访问。这就是门面设计模式存在的意义。
 *    - Tomcat 的门面设计模式示例：Tomcat 中门面设计模式使用的很多，因为 Tomcat 中有很多不同组件，每个组件要相互交互数据，用门面模式隔离数据是个很好的方法。
 *          从图中可以看出 HttpRequestFacade 类封装了 HttpRequest 接口能够提供数据，通过 HttpRequestFacade 访问到的数据都被代理到 HttpRequest 中，
 *          通常被封装的对象都被设为 Private 或者 Protected 访问修饰，以防止在 Façade 中被直接访问。
 *
 * 2、观察者设计模式
 *   - 这种设计模式也是常用的设计方法通常也叫发布 - 订阅模式，也就是事件监听机制，通常在某个事件发生的前后会触发一些操作。
 *   - 观察者模式的原理：观察者模式原理也很简单，就是你在做事的时候旁边总有一个人在盯着你，当你做的事情是它感兴趣的时候，它就会跟着做另外一些事情。但是盯着你的人必须要到你那去登记，不然你无法通知它。
 *      观察者模式通常包含下面这几个角色：- Subject 就是抽象主题：它负责管理所有观察者的引用，同时定义主要的事件操作。
 *                                 - ConcreteSubject 具体主题：它实现了抽象主题的所有定义的接口，当自己发生变化时，会通知所有观察者。
 *                                 - Observer 观察者：监听主题发生变化相应的操作接口。
 *   - Tomcat 的观察者模式示例：Tomcat 中观察者模式也有多处使用，前面讲的控制组件生命周期的 Lifecycle 就是这种模式的体现，还有对 Servlet 实例的创建、Session 的管理、Container 等都是同样的原理。
 *          下面主要看一下 Lifecycle 的具体实现。
 *
 * 3、命令设计模式
 *   - 前面把 Tomcat 中两个核心组件 Connector 和 Container，比作一对夫妻。男的将接受过来的请求以命令的方式交给女主人。对应到 Connector 和 Container，Connector 也是通过命令模式调用 Container 的。
 *   - 命令模式的原理：命令模式主要作用就是封装命令，把发出命令的责任和执行命令的责任分开。也是一种功能的分工。不同的模块可以对同一个命令做出不同解释。
 *
 * 4、责任链模式
 *   - Tomcat 中一个最容易发现的设计模式就是责任链模式，这个设计模式也是 Tomcat 中 Container 设计的基础，整个容器的就是通过一个链连接在一起，这个链一直将请求正确的传递给最终处理请求的那个 Servlet。
 *   - 责任链模式的原理：责任链模式，就是很多对象有每个对象对其下家的引用而连接起来形成一条链，请求在这条链上传递，直到链上的某个对象处理此请求，
 *                    或者每个对象都可以处理请求，并传给下一家，直到最终链上每个对象都处理完。这样可以不影响客户端而能够在链上增加任意的处理节点。
 *   - 通常责任链模式包含下面几个角色：- Handler（抽象处理者）：定义一个处理请求的接口
 *                              - ConcreteHandler（具体处理者）：处理请求的具体类，或者传给下家
 *
 */
public class TomcatLearn {

}
