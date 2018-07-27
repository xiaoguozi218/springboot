package com.example.structure.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: gsh
 * @Date: 2018/7/27 10:33
 * @Description:  Arrays 数组学习
 *
 * 1、Arrays.asList() 转换为的list为什么不能进行add或者remove操作?
 *    - 在使用Arrays.asList()后调用add，remove这些method时出现 java.lang.UnsupportedOperationException异常。
 *    - 原因：这是由于Arrays.asList() 返回java.util.Arrays$ArrayList， 而不是ArrayList。
 *          Arrays$ArrayList和ArrayList都是继承AbstractList，remove，add等 method在AbstractList中是默认throw UnsupportedOperationException而且不作任何操作。
 *    - ArrayList override这些method来对list进行操作，但是Arrays$ArrayList没有override remove()，add()等，所以throw UnsupportedOperationException。
 *    - 解决办法：List list = new ArrayList(Arrays.asList(1,2,3));
 *
 */
public class ArrayLearn {

    public static void main(String[] args) {
//        List list = Arrays.asList(1,2,3);
        List list = new ArrayList(Arrays.asList(1,2,3));
        list.add(5);
    }
}
