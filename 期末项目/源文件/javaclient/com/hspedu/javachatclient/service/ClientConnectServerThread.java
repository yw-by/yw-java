package com.hspedu.javachatclient.service;

import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;


import java.io.ObjectInputStream;

import java.net.Socket;

public class ClientConnectServerThread extends Thread{
    //该线程持有Socket
    private Socket socket;
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }
    public void run(){
        while (true){

            try {
                //System.out.println("客户端线程，等待从读取从服务端发送的消息");
                ObjectInputStream ois =new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象，线程会阻塞在这
                Message message=(Message)ois.readObject();
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {//返回在线列表
                    //取出在线列表信息并显示
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n========当前在线用户列表============");
                    for(int i=0;i<onlineUsers.length;++i){
                        System.out.println("用户： "+onlineUsers[i]);
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                    System.out.println("\n"+message.getSender()+" 对 所有人 说:"+message.getContent());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){//普通消息
                    System.out.println("\n"+message.getSender()+" 对 你 说: "+message.getContent());
                }else if(message.getMesType().equals(MessageType.RET_HISTORICAL_MESSAGES)){
                    String[] HistoricalMessage = message.getContent().split("/ff");
                    System.out.println("\n=============消息记录================");
                    for(int i=0;i<HistoricalMessage.length;++i){
                        System.out.println(HistoricalMessage[i]);
                    }
                }else if(message.getMesType().equals(MessageType.USER_ONLINE)){
                    System.out.println("\n"+message.getSender()+"上线");
                }
                else if(message.getMesType().equals(MessageType.USER_OFFLINE)){
                    System.out.println("\n"+message.getSender()+"下线");
                }else if(message.getMesType().equals(MessageType.RET_ROBOT)){
                    System.out.println(message.getContent());
                }else if(message.getMesType().equals(MessageType.USER_NOT_ONLINE)){
                    System.out.println("用户不在线");
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
