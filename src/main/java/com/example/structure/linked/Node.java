package com.example.structure.linked;
/**
 * @author  xiaoguozi
 * @create  2018/8/20 下午8:51
 * @desc    单链表的结点类
 **/
public class Node {
    protected Node next; //指针域
    protected int data; //数据域

    public Node(int data){
        this.data = data;
    }

}
