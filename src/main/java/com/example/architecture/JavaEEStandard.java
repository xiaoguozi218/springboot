package com.example.architecture;
/**
 * JavaEE的13个核心规范
 * @author  gsh
 * @date  2018/12/19 上午9:41
 * JavaEE的13个核心规范: - JavaEE的13个规范其实就是JavaEE的13个API文档，是一种比较抽象的标准。
 *
 * 1、JDBC（Java Database Connectivity）- JDBC 为访问不同的数据库提供了一种统一的方法，类似的API还有微软的ODBC。
 * 2、JNDI(Java Name and Directory Interface) - JNDI 是在Java中应用于名称和目录服务的API。
 * 3、EJB（Enterprise JavaBean）- JavaBean分为会话bean、实体bean和消息驱动bean。
 * 4、RMI（RemoteMethod Invoke）- 其中，remote遥远的， invoke调用，见名知义，调用遥远的方法。
 *      RMI协议就是调用远程对象上的方法，使用序列化方式在客户端和服务器端传递数据。RMI是被上一个规范EJB使用的更底层的协议，本身比较简单，是分布式的基础所在。
 * 5、Java IDL（Interface Define Language）/CORBA(Common Object Request Broker Architecture)
 * 6、JSP(Java Server Pages) - Jsp页面由html代码和嵌入其中的Java代码构成。JSP是一种动态web资源开发技术。
 * 7、Java Servlet - Servlet是SUN提供的动态web资源的开发技术, 本质上是一段java程序, 这段程序无法独立运行, 需要放在Servlet容器中, 由容器调用才可以执行。
 *                  它是客户请求端和服务响应端的中间层。
 * 8、XML（Extensible Markup Language）- XML是一种可以用来定义其他标记语言的可扩展标记语言。
 * 9、JMS（Java Message Service） - JMS即Java消息服务，是面向消息的中间件通信的应用程序接口（API）。
 *                                它既支持点对点的域，又支持发布/订阅类型的域，比如应用程序之间发送消息，还有分布式系统间的异步通信等。
 * 10、JTA（Java Transaction Architecture）- Java事务结构API。允许应用程序执行分布式事务处理，在两个及以上的网络计算机资源上进行访问并且更新数据。
 * 11、JTS(Java Transaction Service) - JTS：Java事务服务用于分布式事务管理的一套约定或规范。
 * 12、JavaMail -
 *
 *
 **/
public class JavaEEStandard {
}
