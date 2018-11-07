package com.example.java8;

import java.util.Optional;

/**
 * Optional 接口
 *  Optional 不是函数是接口，这是个用来防止NullPointerException异常的辅助类型，这是下一届中将要用到的重要概念，现在先简单的看看这个接口能干什么：
 *  Optional 被定义为一个简单的容器，其值可能是null或者不是null。
 *  在Java 8之前一般某个函数应该返回非空对象但是偶尔却可能返回了null，而在Java 8中，不推荐你返回null而是返回Optional。
 *
 */
public class OptionalTest {

    public static void main(String[] args) {
        Optional<String> optional = Optional.of("gsh");
        System.out.println(optional.isPresent());
        System.out.println(optional.get());
        System.out.println(optional.orElse("xiaoguozi"));
        optional.ifPresent(s -> System.out.println(s.charAt(0)));
    }

}
