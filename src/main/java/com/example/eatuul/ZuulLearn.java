package com.example.eatuul;

/**
 * Created by MintQ on 2018/6/28.
 *  如图所示，在不使用网关的情况下，我们的服务是 直接暴露给服务调用方。当调用方增多，势必需要添加定制化访问权限、校验等逻辑。
 *  当添加API网关后，再第三方调用端和服务提供方之间就创建了 一面墙，这面墙直接与调用方通信进行权限控制。
 *  本文所实现的网关源码抄袭了---Oh,不对，是借鉴。借鉴了Zuul网关的源码，提炼出其核心思路，实现了一套简单的网关源码，博主将其改名为Eatuul。
 *
 *  博主写的手把手系列的文章，目的是在以最简单的方式，揭露出中间件的核心原理，让读者能够迅速了解实现的核心。需要说明的是，这不是源码分析系列的文章，因此写出来的代码，省去了一些复杂的内容，毕竟大家能理解到该中间件的核心原理即可。
 *
 *  《*》设计思路：
 *      先大致说一下，就是定义一个Servlet接收请求。然后经过preFilter(封装请求参数),routeFilter(转发请求)，postFilter(输出内容)。三个过滤器之间，共享request、response以及其他的一些全局变量。
 *      和真正的Zuul的区别?
         主要区别有如下几点：
         (1)Zuul中在异常处理模块，有一个ErrorFilter来处理，博主在实现的时候偷懒了，略去。
         (2)Zuul中PreFilters,RoutingFilters,PostFilters默认都实现了一组。
 *          博主总不可能每一个都给你们实现一遍吧。所以偷懒了，每种只实现一个。但是调用顺序还是不变，按照PreFilters->RoutingFilters->PostFilters的顺序调用。
 *       (3)在routeFilters确实有转发请求的Filter,然而博主偷天换日了，改用RestTemplate实现。
 *
 *
 *
 *
 */
public class ZuulLearn {

}
