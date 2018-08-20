package com.example.structure.linked;

/**
 * @author  xiaoguozi
 * @create  2018/8/19 下午9:25
 * @desc    链表学习 -
 *
 * 1、判断一个单链表中是否有环？
 *   - 思路：如果一个单链表中有环，用一个指针去遍历，永远不会结束，所以可以用两个指针，一个指针一次走一步，另一个指针一次走两步，
 *          如果存在环，则这两个指针会在环内相遇，时间复杂度为O(n)。
 *
 **/
public class LinkedLearn {

    public static void main(String[] args) {

    }

    public static boolean hasCircle(LNode L)
    {
        LNode slow=L;//slow表示从头结点开始每次往后走一步的指针
        LNode fast=L;//fast表示从头结点开始每次往后走两步的指针
        while(fast!=null && fast.next!=null)
        {
            if(slow==fast) return true;//p与q相等，单链表有环
            slow=slow.next;
            fast=fast.next.next;
        }
        return false;
    }

}
