package com.hspedu.javaserver.service;

import java.util.HashMap;
import java.util.Iterator;

//该类用于管理和客户端通信的线程
public class ManageClientTreads {//userID是key，value是线程
    private static HashMap<String,ServerConnectClientThread> hm =new HashMap<>();

    //添加线程对象到hm集合
    public static void addClientThread(String userID, ServerConnectClientThread serverConnectClientThread){
        hm.put(userID, serverConnectClientThread);

    }
    //根据userID返回对应线程
    public static ServerConnectClientThread getServerConnectClientThread(String userID){
        return hm.get(userID);
    }
    //从集合中移除某个线程对象
    public static void removeServerConnectClientThread(String userID){
        hm.remove(userID);
    }
    //返回在线用户列表
    public static String getOnlineUsers(){
        //集合遍历，遍历 hashmap的key
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
