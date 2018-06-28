package com.example.eatuul.http;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MintQ on 2018/6/28.
 * 这个是网关的入口，逻辑也十分简单，分为三步：
     (1)将request,response放入threadlocal中
     (2)执行三组过滤器
     (3)清除threadlocal中的的环境变量
 *
 */
@WebServlet(name = "eatuul", urlPatterns = "/*")
public class EatuulServlet extends HttpServlet {
    private EatRunner eatRunner = new EatRunner();

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //将request，和response放入上下文对象中
        eatRunner.init(req, resp);
        try {
            eatRunner.preRoute();            //执行前置过滤
            eatRunner.route();            //执行过滤
            eatRunner.postRoute();      //执行后置过滤
        } catch (Throwable e) {
            RequestContext.getCurrentContext().getResponse()
                    .sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } finally {
            //清除变量
            RequestContext.getCurrentContext().unset();
        }
    }


}
