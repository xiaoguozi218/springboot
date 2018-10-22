package com.example.concurrent.reflect;

import lombok.Data;

@Data
public class Person implements Interface{

    private String id ;

    private String name ;

    public String age ;

    //构造函数1
    public Person( ){

    }

    //构造函数2
    public Person( String id ){
        this.id = id ;
    }

    //构造函数3
    public Person( String id  , String name ){
        this.id = id ;
        this.name = name ;
    }

    /**
     * 静态方法
     */
    public static void update(){

    }

    @Override
    public void read() {

    }
}
