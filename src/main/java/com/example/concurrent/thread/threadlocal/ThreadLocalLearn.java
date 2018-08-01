package com.example.concurrent.thread.threadlocal;

/**
 * @Auther: gsh
 * @Date: 2018/8/1 15:20
 * @Description:
 *
 *
 *《ThreadLocal在框架中的例子》：
 *  1、在著名的框架Hiberante中，我们来看一下数据库连接的代码：
 *      private static final ThreadLocal threadSession = new ThreadLocal();
 *      public static Session getSession() throws InfrastructureException {
            Session s = (Session) threadSession.get();
            try {
                if (s == null) {
                    s = getSessionFactory().openSession();
                    threadSession.set(s);
                }
            } catch (HibernateException ex) {
                throw new InfrastructureException(ex);
            }
                return s;
        }
 *  2、Spring处理事务，看看一个类TransactionSynchronizationManager
 *
 */
public class ThreadLocalLearn {
}
