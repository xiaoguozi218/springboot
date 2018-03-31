package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/3/31.
 */
@RestController
public class HelloController {


    @RequestMapping("/hello")
    public String hello() {
        return "hello,this is a springboot demo";
    }

}
