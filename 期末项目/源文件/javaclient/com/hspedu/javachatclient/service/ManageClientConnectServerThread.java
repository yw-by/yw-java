package com.hspedu.javachatclient.service;

import java.util.HashMap;


//�������ͻ������ӵ��������˵��̵߳���
public class ManageClientConnectServerThread {
    //���ǰѶ���̷߳���HashMap���ϣ�ley�����û�ID��value�����߳�
    private static HashMap<String,ClientConnectServerThread> hm =new HashMap<>();

    //��ĳ���̼߳��뵽����
    public static void addClientConnectServerThread(String userID,ClientConnectServerThread clientConnectServerThread){
        hm.put(userID,clientConnectServerThread);
    }
    //ͨ��userid�����Եõ���Ӧ�߳�
    public static ClientConnectServerThread getClientConnectServerThread(String userID){
        return hm.get(userID);
    }

}
