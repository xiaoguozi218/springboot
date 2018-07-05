package com.example.controller;

import com.example.model.Person;
import com.example.pattern.strategy.StrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @RequestMapping(value = "hello/person/{id}", method = RequestMethod.GET)
    public Person findOneperson(@PathVariable("id") Long id) {
        Person person = new Person();
        person.setId(id);
        person.setName("张三");
        return person;
    }

    @RequestMapping(value = "hello/person",method = RequestMethod.POST)
    public void createperson(@RequestBody Person person) {
        System.out.println(person.getName());
        System.out.println("createperson success");
    }

    @Autowired
    private StrategyContext strategyContext;

    @RequestMapping("calculatePrice")
    public @ResponseBody BigDecimal calculatePrice(String memberLevel) {
        return strategyContext.calculatePrice(memberLevel);
    }


}
