package com.hspedu.javachatclient.service;

import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;


import java.io.ObjectInputStream;

import java.net.Socket;

public class ClientConnectServerThread extends Thread{
    //���̳߳���Socket
    private Socket socket;
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }
    public void run(){
        while (true){

            try {
                //System.out.println("�ͻ����̣߳��ȴ��Ӷ�ȡ�ӷ���˷��͵���Ϣ");
                ObjectInputStream ois =new ObjectInputStream(socket.getInputStream());
                //���������û�з���Message�����̻߳���������
                Message message=(Message)ois.readObject();
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {//���������б�
                    //ȡ�������б���Ϣ����ʾ
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n========��ǰ�����û��б�============");
                    for(int i=0;i<onlineUsers.length;++i){
                        System.out.println("�û��� "+onlineUsers[i]);
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                    System.out.println("\n"+message.getSender()+" �� ������ ˵:"+message.getContent());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){//��ͨ��Ϣ
                    System.out.println("\n"+message.getSender()+" �� �� ˵: "+message.getContent());
                }else if(message.getMesType().equals(MessageType.RET_HISTORICAL_MESSAGES)){
                    String[] HistoricalMessage = message.getContent().split("/ff");
                    System.out.println("\n=============��Ϣ��¼================");
                    for(int i=0;i<HistoricalMessage.length;++i){
                        System.out.println(HistoricalMessage[i]);
                    }
                }else if(message.getMesType().equals(MessageType.USER_ONLINE)){
                    System.out.println("\n"+message.getSender()+"����");
                }
                else if(message.getMesType().equals(MessageType.USER_OFFLINE)){
                    System.out.println("\n"+message.getSender()+"����");
                }else if(message.getMesType().equals(MessageType.RET_ROBOT)){
                    System.out.println(message.getContent());
                }else if(message.getMesType().equals(MessageType.USER_NOT_ONLINE)){
                    System.out.println("�û�������");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }



}
