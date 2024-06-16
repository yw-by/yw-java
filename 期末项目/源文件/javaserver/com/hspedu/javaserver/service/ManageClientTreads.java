package com.hspedu.javaserver.service;

import java.util.HashMap;
import java.util.Iterator;

//�������ڹ���Ϳͻ���ͨ�ŵ��߳�
public class ManageClientTreads {//userID��key��value���߳�
    private static HashMap<String,ServerConnectClientThread> hm =new HashMap<>();

    //����̶߳���hm����
    public static void addClientThread(String userID, ServerConnectClientThread serverConnectClientThread){
        hm.put(userID, serverConnectClientThread);

    }
    //����userID���ض�Ӧ�߳�
    public static ServerConnectClientThread getServerConnectClientThread(String userID){
        return hm.get(userID);
    }
    //�Ӽ������Ƴ�ĳ���̶߳���
    public static void removeServerConnectClientThread(String userID){
        hm.remove(userID);
    }
    //���������û��б�
    public static String getOnlineUsers(){
        //���ϱ��������� hashmap��key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList="";
        while (iterator.hasNext()){
            onlineUserList+=iterator.next().toString()+" ";
        }
        return onlineUserList;
    }
public static boolean CheckUserIsOnline(String UserID){
        boolean b=false;
        Iterator<String> iterator=hm.keySet().iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(UserID)){
                b=true;
                break;
            }
        }
        return b;
}
    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }
}
