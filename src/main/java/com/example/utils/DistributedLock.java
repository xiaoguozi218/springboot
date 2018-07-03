package com.example.utils;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by MintQ on 2018/6/4.
 *
 * 基于ZooKeeper分布式锁的流程

     在zookeeper指定节点（locks）下创建临时顺序节点node_n
     获取locks下所有子节点children
     对子节点按节点自增序号从小到大排序
     判断本节点是不是第一个子节点，若是，则获取锁；若不是，则监听比该节点小的那个节点的删除事件
     若监听事件生效，则回到第二步重新进行判断，直到获取到锁
     具体实现

     下面就具体使用java和zookeeper实现分布式锁，操作zookeeper使用的是apache提供的zookeeper的包。

     通过实现Watch接口，实现process(WatchedEvent event)方法来实施监控，使CountDownLatch来完成监控，在等待锁的时候使用CountDownLatch来计数，等到后进行countDown，停止等待，继续运行。
     以下整体流程基本与上述描述流程一致，只是在监听的时候使用的是CountDownLatch来监听前一个节点。
 *
 *
 *一线架构师详解 ZooKeeper 功能以及工作原理:
 *  ZooKeeper是干啥的？~ ZooKeeper是一个 分布式协调服务，他为分布式应用提供了高效且可靠的分布式协调服务，提供了诸如统一命名空间服务，配置服务和分布式锁等分布式基础服务。
 *
 *  ZooKeeper基本概念:
 *      ~集群角色:  和Paxos算法中的集群角色类型，ZooKeeper中包含Leader、Follower和Observer三个角色；
 *                 通过一次选举过程，被选举的机器节点被称为Leader，Leader机器为客户端提供读和写服务；
 *                 Follower和Observer是集群中的其他机器节点，唯一的区别就是：Observer不参与Leader的选举过程，也不参与写操作的过半写成功策略。
 *      ~会话:    会话就是一个客户端与服务器之间的一个TCP长连接。客户端和服务器的一切交互都是通过这个长连接进行的；
 *                会话会在客户端与服务器断开链接后，如果经过了设点的sessionTimeout时间内没有重新链接后失效。
 *      ~节点:    节点在ZeeKeeper中包含两层含义：
 *                  1、集群中的一台机器，我们成为机器节点；
 *                  2、ZooKeeper数据模型中的 数据单元，我们成为数据节点（ZNode）。
 *                      ZooKeeper的数据模型是内存中的一个ZNode数，由斜杠(/)进行分割的路径，就是一个ZNode，每个ZNode上除了保存自己的数据内容，还保存一系列属性信息；
 *                      ZooKeeper中的数据节点分为两种：持久节点: 所谓的持久节点是指一旦这个ZNode创建成功，除非主动进行ZNode的移除操作，节点会一直保存在ZooKeeper上；
 *                                                  临时节点: 而临时节点的生命周期是跟客户端的会话相关联的，一旦客户端会话失效，这个会话上的所有临时节点都会被自动移除。
 *      ~版本: ZooKeeper为每一个ZNode节点维护一个叫做Stat的数据结构，在Stat中维护了节点相关的三个版本：
 *              1、当前ZNode的版本 version
 *              2、当前ZNode子节点的版本 cversion
 *              3、当前ZNode的ACL(Access Control Lists)版本 aversion
 *      ~监听器Watcher： ZooKeeper允许用户在指定节点上注册一些Watcher，并且在一些特定事件触发的时候，ZooKeeper会通过事件通知到感兴趣的客户端上。
 *
 *      ~ACL（Access Control Lists）：ZooKeeper中定义了5中控制权限： 其中 CREATE 和 DELETE 这两种权限都是针对子节点的权限控制。
 *                                      1、CREATE：创建子节点的权限
 *                                      2、READ：获取节点数据和子节点列表的权限
 *                                      3、WRITE：跟新节点数据的权限
 *                                      4、DELETE：删除子节点的权限
 *                                      5、ADMIN：设置节点ACL的权限。
 *
 *  ZooKeeper的数据模型: 上面有提到ZooKeeper的数据模型是一个ZNode节点树，是一个类型与标准文件系统的层次结构，也是使用斜杠(/)进行分割.
 *      在ZooKeeper中每一个节点都可以使用其路径唯一标识，如节点p_1的标识为：/app1/p_1
 *      每个ZNode节点都可以存储自己的数据，还可以拥有自己的子节点目录。
 *
 *
 *
 *
 */
public class DistributedLock implements Lock, Watcher {

    private ZooKeeper zk = null;
    // 根节点
    private String ROOT_LOCK = "/locks";
    // 竞争的资源
    private String lockName;
    // 等待的前一个锁
    private String WAIT_LOCK;
    // 当前锁
    private String CURRENT_LOCK;
    // 计数器
    private CountDownLatch countDownLatch;
    private int sessionTimeout = 30000;
    private List<Exception> exceptionList = new ArrayList<Exception>();

    /**
     * 配置分布式锁
     * @param config 连接的url
     * @param lockName 竞争资源
     */
    public DistributedLock(String config, String lockName) {
        this.lockName = lockName;
        try {
            // 连接zookeeper
            zk = new ZooKeeper(config, sessionTimeout, this);
            Stat stat = zk.exists(ROOT_LOCK, false);
            if (stat == null) {
                // 如果根节点不存在，则创建根节点
                zk.create(ROOT_LOCK, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    // 节点监视器
    @Override
    public void process(WatchedEvent event) {
        if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
        }
    }

    @Override
    public void lock() {
        if (exceptionList.size() > 0) {
            throw new LockException(exceptionList.get(0));
        }
        try {
            if (this.tryLock()) {
                System.out.println(Thread.currentThread().getName() + " " + lockName + "获得了锁");
                return;
            } else {
                // 等待锁
                waitForLock(WAIT_LOCK, sessionTimeout);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tryLock() {
        try {
            String splitStr = "_lock_";
            if (lockName.contains(splitStr)) {
                throw new LockException("锁名有误");
            }
            // 创建临时有序节点
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/" + lockName + splitStr, new byte[0],
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(CURRENT_LOCK + " 已经创建");
            // 取所有子节点
            List<String> subNodes = zk.getChildren(ROOT_LOCK, false);
            // 取出所有lockName的锁
            List<String> lockObjects = new ArrayList<String>();
            for (String node : subNodes) {
                String _node = node.split(splitStr)[0];
                if (_node.equals(lockName)) {
                    lockObjects.add(node);
                }
            }
            Collections.sort(lockObjects);
            System.out.println(Thread.currentThread().getName() + " 的锁是 " + CURRENT_LOCK);
            // 若当前节点为最小节点，则获取锁成功
            if (CURRENT_LOCK.equals(ROOT_LOCK + "/" + lockObjects.get(0))) {
                return true;
            }

            // 若不是最小节点，则找到自己的前一个节点
            String prevNode = CURRENT_LOCK.substring(CURRENT_LOCK.lastIndexOf("/") + 1);
            WAIT_LOCK = lockObjects.get(Collections.binarySearch(lockObjects, prevNode) - 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        try {
            if (this.tryLock()) {
                return true;
            }
            return waitForLock(WAIT_LOCK, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 等待锁
    private boolean waitForLock(String prev, long waitTime) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(ROOT_LOCK + "/" + prev, true);

        if (stat != null) {
            System.out.println(Thread.currentThread().getName() + "等待锁 " + ROOT_LOCK + "/" + prev);
            this.countDownLatch = new CountDownLatch(1);
            // 计数等待，若等到前一个节点消失，则precess中进行countDown，停止等待，获取锁
            this.countDownLatch.await(waitTime, TimeUnit.MILLISECONDS);
            this.countDownLatch = null;
            System.out.println(Thread.currentThread().getName() + " 等到了锁");
        }
        return true;
    }

    @Override
    public void unlock() {
        try {
            System.out.println("释放锁 " + CURRENT_LOCK);
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }


    public class LockException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public LockException(String e){
            super(e);
        }
        public LockException(Exception e){
            super(e);
        }
    }


}
