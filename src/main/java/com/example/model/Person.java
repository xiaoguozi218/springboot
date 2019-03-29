package com.example.model;

import lombok.Data;

/**
 * Created by MintQ on 2018/7/4.
 *
 * 该实体类是为了在控制器层进行传递参数使用
 */
@Data
public class Person {

    private long id;

    private String name;

    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
