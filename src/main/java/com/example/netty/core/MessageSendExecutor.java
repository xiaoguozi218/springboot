package com.example.netty.core;

/**
 * @filename:MessageSendExecutor.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Rpc客户端执行模块
 * @author tangjie
 * @version 1.0
 *
 */

import java.lang.reflect.Proxy;

public class MessageSendExecutor {

    private RpcServerLoader loader = RpcServerLoader.getInstance();

    public MessageSendExecutor(String serverAddress) {
        loader.load(serverAddress);
    }

    public void stop() {
        loader.unLoad();
    }

    public static <T> T execute(Class<T> rpcInterface) {
        return (T) Proxy.newProxyInstance(
                rpcInterface.getClassLoader(),
                new Class<?>[]{rpcInterface},
                new MessageSendProxy<T>(rpcInterface)
        );
    }
}