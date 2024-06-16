package com.hspedu.javachatclient.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

//该类提供与消息相关的方法
public class MessageClientService {
    //群聊方法
    public void SendMessageToAll(String content2,String senderID){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL);
        message.setSender(senderID);
        message.setSendTime(new Date().toString());
        message.setContent(content2);
        System.out.println("你 对所有人说: "+content2);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderID).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //私聊方法
    public void SendMessageToOne(String content,String  senderID,String getterID){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间
        System.out.println("你  给 "+getterID+" 发送: "+content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderID).getSocket().getOutputStream());
        oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //查看历史消息
    public void ViewMessages(String userid){
        Message message =new Message();
        message.setMesType(MessageType.VIEW_HISTORICAL_MESSAGES);
        message.setSender(userid);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(userid).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void AskRobot(String userid,String choose){
        Message message=new Message();
        message.setSender(userid);
        message.setContent(choose);
        message.setMesType(MessageType.ASK_ROBOT);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(userid).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
