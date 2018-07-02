package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/3/31.
 *
 * Spring框架在国内的软件开发早已是全面普及了，开发中使用Spring主要是为了它的三种特点：
 *  1. IOC容器管理各层的组件
 *  2. AOP配置声明式事务、日志、异常
 *  3. 整合其它框架
 *
 */
@RestController
public class HelloController {


    @RequestMapping("/hello")
    public String hello() {
        return "hello,this is a springboot demo!";
    }

}
