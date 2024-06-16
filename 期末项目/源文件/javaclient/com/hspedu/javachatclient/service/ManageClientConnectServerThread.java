package com.hspedu.javachatclient.service;

import java.util.HashMap;


//该类管理客户端连接到服务器端的线程的类
public class ManageClientConnectServerThread {
    //我们把多个线程放在HashMap集合，ley就是用户ID，value就是线程
    private static HashMap<String,ClientConnectServerThread> hm =new HashMap<>();

    //将某个线程加入到集合
    public static void addClientConnectServerThread(String userID,ClientConnectServerThread clientConnectServerThread){
        hm.put(userID,clientConnectServerThread);
    }
    //通过userid都可以得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userID){
        return hm.get(userID);
    }

}
