package com.example.io;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import sun.misc.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用java.io  和java.net 中的同步、阻塞式API，可以简单实现。
 * Created by MintQ on 2018/6/1.
 */
public class DemoServer extends  Thread {

    private ServerSocket serverSocket;

    private volatile ExecutorService executorService;

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(0);
            executorService = Executors.newFixedThreadPool(8);
            while (true) {

                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket);
//                requestHandler.start();
                executorService.execute(requestHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //简化实现，不做读取，直接发送字符串
    class RequestHandler extends Thread{
        private Socket socket;
        RequestHandler(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("hello world!");
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DemoServer server = new DemoServer();
        server.start();
        try {
            Socket client = new Socket(InetAddress.getLocalHost(),server.getPort());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
