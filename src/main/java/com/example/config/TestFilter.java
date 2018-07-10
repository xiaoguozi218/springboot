package com.example.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created by MintQ on 2018/7/4.
 *
 * 《*》CAS实现单点登录（SSO）过程浅析：
 *     一、何谓单点登录
 *         单点登录（Single Sign On），简称为 SSO，简单理解就是在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统。
 *     二、何谓CAS
 *        CAS（Central Authentication Service）是耶鲁大学的一个开源项目，旨在为web应用系统提供一种可靠的单点登录解决方案。
 *        采用CAS最大的是从安全性角度来考虑的，用户在CAS录入用户名和密码之后通过ticket进行认证，不会在网上传输密码，保证安全性。
 *     三、CAS中的关键词理解
 *        ~ST（Service Ticket）：服务票据，服务的唯一标识码，由TGT生成ST，返回给用户，接着拿着生成的ST去访问service，service又会把ST拿到CAS系统去验证，验证通过后才允许用户访问该资源。
 *        ~TGC（ Ticket Granting Cookie）：CAS系统用来识别用户身份的凭证。
 *        ~TGT（Ticket Grangting Ticket）：授权票据，获取这TGT之后才能申请服务票据 （ST），用户如果在CAS系统认证成功之后，就会生成TGC写入浏览器，同时也生成一个TGT，TGT对象的id就是cookie值。
 *                                         之后每次请求过来通过此cookie来从缓存获取TGT，就不用提交身份认证信息（Credentials）。
 *        ~Session：各个应用系统会创建自己的session表示是否登录，而这里的每个session都是ST验证通过之后组装生成的。
 *
 */
@WebFilter(urlPatterns = "/*",filterName = "TestFilter")
public class TestFilter implements javax.servlet.Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("TestFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TestFilter doFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
