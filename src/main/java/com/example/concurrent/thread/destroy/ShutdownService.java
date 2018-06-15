package com.example.concurrent.thread.destroy;

import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MintQ on 2018/6/15.
 */
@Service
public class ShutdownService {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownService.class);

    private final List<Hook> hooks;

    public ShutdownService() {
        logger.debug("Creating shutdown service");
        hooks = new ArrayList<Hook>();
        createShutdownHook();
    }

    /**
     * Protected for testing
     */
    @VisibleForTesting
    protected void createShutdownHook() {
        ShutdownDaemonHook shutdownHook = new ShutdownDaemonHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    protected class ShutdownDaemonHook extends Thread {

        /**
         * 循环并使用hook关闭所有后台线程
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            logger.info("Running shutdown sync");

            for (Hook hook : hooks) {
                hook.shutdown();
            }
        }
    }

    /**
     * 创建hook class的新实例
     */
    public Hook createHook(Thread thread) {

        thread.setDaemon(true);
        Hook retVal = new Hook(thread);
        hooks.add(retVal);
        return retVal;
    }

    @VisibleForTesting
    List<Hook> getHooks() {
        return hooks;
    }

}
