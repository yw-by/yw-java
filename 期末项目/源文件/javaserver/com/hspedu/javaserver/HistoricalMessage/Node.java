package com.hspedu.javaserver.HistoricalMessage;

import com.hspedu.javachatcommon.Message;
//�������ڴ�����Ӧ�û�����Ϣ����
public class Node{//���
    public Node(String userID, Message message, Node next) {
        this.userID = userID;
        this.message = message;
        this.next=next;
    }
    public Node(String userID,Node next){
        this.userID=userID;
        this.next=null;
    }
    String userID;//�û���
    Message message;//��Ϣ
    Node next;//��һ�����
    public Node getNext(){return this.next;}

}
