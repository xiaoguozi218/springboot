package com.example.annotation;

/**
 * Created by MintQ on 2018/7/12.
 *
 * 《*》自定义注解加AOP切面实现方法的入参和出参的打印
 *  1.什么是自定义注解，简单来说就是自己定义的 - 类似于实现 @RequestMapping这种注解的功能
 *  2.怎么实现自定义注解 - 自定义注解需要使用到 元注解
 *      @Target(ElementType.METHOD)         - 方法级别@Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME) - 保留至运行时@Retention(RetentionPolicy.RUNTIME)
        @Documented
        public @interface MyLog {
            String value();
        }
        AOP切面作用：AOP技术，可以将一些系统性相关的编程工作，独立提取出来，独立实现，然后通过切面切入进系统
            简单点，就是 不改变原来的代码，实现一些统一的业务逻辑。


 */
public class AnnotationLearn {
}
