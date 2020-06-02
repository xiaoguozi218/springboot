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

    public Node head; //定义一个头结点
    public Node current; //当前结点

    //添加元素
    public void add(int data){
        //判断链表是否为空
        if(head == null) {  //如果头结点为空,说明这个链表还没有被创建
            head = new Node(data);
            current = head;
        } else {
            //创建新的结点
            Node node = new Node(data);
            //放在当前结点的后面
            current.next = node;
            //把链表的当前索引向后移动一位 （引用变量）
            current = current.next;
        }
    }

    //获取单链表的长度
    public int getLength() {
        //头结点为空则返回0
        if (head == null) {
            return 0;
        } else {
            int length=0;
            Node current = head;
            while(current != null) {
                current = current.next;
                length++;
            }
            return length;
        }
    }


    //打印链表
    public void printList(Node head){
        Node tmp = head;
        while(tmp!=null) {
            System.out.print(tmp.data+" ");
            tmp = tmp.next;
        }
        System.out.println();
    }


    //删除第index个结点
    public boolean deleteNode(int index){
        if (index<1||index>this.getLength())
            return false;
        if(index==1){
            head=head.next;
            return true;
        }
        int i = 1;
        Node preNode = head;
        Node curNode = preNode.next;
        while(curNode!=null) {
            if(i==index) {
                preNode.next=curNode.next;
                return true;
            }
            preNode = curNode;
            curNode = curNode.next;
            i++;
        }
        return false;
    }



    //链表反转 遍历实现
    public static Node reverse(Node head) {
        if (head==null||head.next==null)
            return head;
        Node pre = head; //上一结点
        Node cur = head.next; //当前结点
        Node tmp ;//临时结点,用于保存当前指针域
        while(cur!=null) {
            tmp = cur.next;
            cur.next = pre;  //反转指向
            //指针向下移动
            pre = cur;
            cur = tmp;
        }
        head.next=null ;//将头结点设空
        return pre;
    }

    //判断单链表是否有环,我们用两个指针去遍历：
    //first指针每次走一步，second指针每次走两步，如果first指针和second指针相遇，说明有环。
    public boolean hasCycle(Node head) {
        if (head == null) {
            return false;
        }
        Node first = head;
        Node second = head;
        while (second != null) {
            first = first.next;   //first指针走一步
            second = second.next.next;  //second指针走两步
            if (first == second) {  //一旦两个指针相遇，说明链表是有环的
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        LinkedLearn list = new LinkedLearn();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1);
        list.printList(list.head);
        Node newHead = reverse(list.head);
        list.printList(newHead);

        System.out.println(list.hasCycle(newHead));
    }
}
