package com.example.test.interview;

/**
 * Created by MintQ on 2018/7/10.
 *
 * 《*》三大框架方面问题
 *    1、Spring 事务的隔离性，并说说每个隔离性的区别
 *       事务隔离级别: 1、读未提交 （会出现脏读, 不可重复读。基本不使用）
 *                   2、读已提交 （会出现不可重复读和幻读）               Oracle: 默认使用的是READ COMMITTED
 *                   3、可重复读 （会出现幻读）                         MYSQL: 默认为REPEATABLE_READ级别
 *                   4、可串行化读
 *       ~脏读 : 一个事务读取到另一事务 未提交 的更新数据
 *       ~不可重复读 : 在同一事务中, 多次读取同一数据返回的结果有所不同, 换句话说, 后续读取可以读到另一事务已提交的更新数据.
 *                      相反, "可重复读"在同一事务中多次读取数据时, 能够保证所读数据一样, 也就是后续读取不能读到另一事务已提交的更新数据
 *       ~幻读 : 一个事务读到另一个事务已提交的insert数据
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
 * 《*》负载均衡、集群相关
 *     1、Nginx+Tomcat+Redis实现负载均衡、资源分离、session共享
 *
 *
 *
 */
public class InterviewQuestions {

}
