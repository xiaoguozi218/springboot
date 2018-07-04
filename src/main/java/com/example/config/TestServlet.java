package com.example.config;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MintQ on 2018/7/4.
 *
 */
@WebServlet(name = "TestServlet",urlPatterns = "/testServlet")
public class TestServlet extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("TestServlet into");
        res.getWriter().print("invoke TestServlet");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        this.doGet(req,res);
    }
}
