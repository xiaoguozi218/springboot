package com.example.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by MintQ on 2018/7/4.
 */
@WebListener
public class TestListener implements ServletContextListener,ServletRequestListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("TestListener init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("TestListener requestInitialized");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("TestListener requestInitialized");
    }
}
