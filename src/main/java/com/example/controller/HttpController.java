package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MintQ on 2018/7/4.
 *
 * 网页http请求的整个过程:
 *  1、打开浏览器，地址栏输入blog.csdn.net
 *  2、开始进行域名解析（DNS）
 *      ~1、浏览器自身搜dns缓存 搜blog.csdn.net有没有缓存 看看有没有过期，如果过期就这个结束；
 *      ~2、搜索操作系统 自身的dns缓存；
 *      ~3、读取本地的host文件；
 *      ~4、浏览器发起一个dns的一个系统调用
 *  3、浏览器获得域名对应的ip地址后 发起http三次握手
 *  4、tcp/ip 链接建立起来后，浏览器就可以向服务器发送http 请求 。
 *  5、服务器端接受到请求，根据路径参数，经过后端的一些处理之后，把处理后的一个结果数据返回给浏览器，如果是一个完整的网页，就是把完整的html页面代码返回给浏览器。
 *  6、浏览器拿到html页面代码，解析和渲染页面，里面的 js、css图片资源都需要经过上面的步骤。
 *  7、浏览器拿到资源对页面进行渲染，最终把一个完整的页面呈现给用户。
 *
 */
@RestController
public class HttpController {

    private static final Logger logger = LoggerFactory.getLogger(HttpController.class);
}
