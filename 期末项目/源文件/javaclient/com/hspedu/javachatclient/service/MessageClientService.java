package com.hspedu.javachatclient.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

//�����ṩ����Ϣ��صķ���
public class MessageClientService {
    //Ⱥ�ķ���
    public void SendMessageToAll(String content2,String senderID){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL);
        message.setSender(senderID);
        message.setSendTime(new Date().toString());
        message.setContent(content2);
        System.out.println("�� ��������˵: "+content2);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderID).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //˽�ķ���
    public void SendMessageToOne(String content,String  senderID,String getterID){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setContent(content);
        message.setSendTime(new Date().toString());//����ʱ��
        System.out.println("��  �� "+getterID+" ����: "+content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderID).getSocket().getOutputStream());
        oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //�鿴��ʷ��Ϣ
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
