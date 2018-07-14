package com.example.structure.stack_queue;

import java.util.Stack;

/**
 * @author  xiaoguozi
 * @create  2018/7/14 下午5:20
 * @desc    用栈实现队列 - Implement Queue using Stacks (Easy)
 * s
 **/
public class MyQueue {

    private Stack<Integer> in = new Stack<>();
    private Stack<Integer> out = new Stack<>();

    public void push(int x) {
        in.push(x);
    }

    public int pop() {
        in2out();
        return out.pop();
    }

    /**
     * @author  xiaoguozi
     * @create  2018/7/14 下午5:28
     * @desc    peek方法返回队列首个元素，但不移除，若队列为空，返回null
     **/
    public int peek() {
        in2out();
        return out.peek();
    }

    private void in2out() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }
    }

    public boolean empty() {
        return in.isEmpty() && out.isEmpty();
    }

}
