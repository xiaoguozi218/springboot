package com.example.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring Boot是如何实现日志的？ - 门面模式、
 *  1、门面模式 - 说到日志框架不得不说门面模式。门面模式，其核心为外部与一个子系统的通信必须通过一个统一的外观对象进行，使得子系统更易于使用。
 *     简单来说，该模式就是把一些 复杂的流程 封装成一个接口供给外部用户更简单的使用。
 *     这个模式中，涉及到三个角色：
 *  2、为什么要使用门面模式?
 *      Spring Boot 底层默认选用的就是 SLF4j 和 Logback 来实现日志输出。
 *  3、
 */
public class LogLearn {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogLearn.class);



}
