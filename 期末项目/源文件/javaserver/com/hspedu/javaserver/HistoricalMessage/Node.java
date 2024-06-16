package com.hspedu.javaserver.HistoricalMessage;

import com.hspedu.javachatcommon.Message;
//该类用于创建对应用户的信息链表
public class Node{//结点
    public Node(String userID, Message message, Node next) {
        this.userID = userID;
        this.message = message;
        this.next=next;
    }
    public Node(String userID,Node next){
        this.userID=userID;
        this.next=null;
    }
    String userID;//用户名
    Message message;//消息
    Node next;//下一个结点
    public Node getNext(){return this.next;}

}
