package com.hspedu.javachatclient.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import com.hspedu.javachatcommon.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
//该类完成用户登录验证和用户注册等功能
public class UserClientService {
    private User u=new User();
    private Socket socket;

    public boolean SignUp(String userID, String pwd){
        boolean b= false;
        //创建一个User对象
        u.setUserid(userID);
        u.setPasswd(pwd);
        u.setUsertype(0);
        try {
            //连接服务端，发送u对象
            Socket socket= new Socket(InetAddress.getByName("127.0.0.1"),9999);
            //得到ObjectOutputStream对象
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象

            //读取从服务端回复的Message对象
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            Message ms =(Message) ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_SIGN_UP_SUCCEED)){//注册成功
                b=true;
                System.out.println("注册成功");

            }else{
                //如果登录失败，奴能创建线程，关闭socket
                System.out.println("注册失败，该用户名已存在");


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;

    }
    //根据UserID和pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userID,String pwd){
        boolean b=false;
    //创建一个User对象
        u.setUserid(userID);
        u.setPasswd(pwd);
        u.setUsertype(1);

        try {
            //连接服务端，发送u对象
            Socket socket= new Socket(InetAddress.getByName("127.0.0.1"),9999);
            //得到ObjectOutputStream对象
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象

            //读取从服务端回复的Message对象
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            Message ms =(Message) ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){//登录成功
                b=true;
                //创建一个和服务器端保持通信的线程-> 创建一个类 ClientConnectServerThread

                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //启动客户端的线程
                clientConnectServerThread.start();
                //为了后面客户端的扩展，我们将集合放入集合中
                ManageClientConnectServerThread.addClientConnectServerThread(userID,clientConnectServerThread);

                }else{
                //如果登录失败，不能创建线程，关闭socket
                socket.close();

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    //向服务器端请求在线用户列表
    public void onlineFriendList(){
        Message message =new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserid());
        //发送给服务器

        try {
            //从线程集合根据uid取出对应线程
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserid());
            //从对应线程中取出socket
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());//从socket取出输出流
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //编写方法退出客户端，并给服务端发送一个退出系统的message
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserid());//一定要指明是哪个客户端id
        //发送message
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserid()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserid()+"退出系统");
            System.exit(0);//退出系统
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
