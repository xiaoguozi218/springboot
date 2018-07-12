package com.example.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by MintQ on 2018/7/12.
 *
 * 1、定义切面
 * 2、然后定义切点 - 这边我们的切点就不是对应的某个包下所有方法，而是切到我们对应的自定义注解
 *
 */
@Aspect
@Component
public class OperateAspect  {

    @Pointcut("@annotation(com.example.annotation.MyLog)")
    public void annotationPointCut() {
    }

    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint){
        MethodSignature sign =  (MethodSignature)joinPoint.getSignature();
        Method method = sign.getMethod();
        MyLog annotation = method.getAnnotation(MyLog.class);
        System.out.print("打印："+annotation.value()+" 前置日志");
    }
}
