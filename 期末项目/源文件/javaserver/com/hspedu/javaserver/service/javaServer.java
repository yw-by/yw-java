package com.hspedu.javaserver.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import com.hspedu.javachatcommon.User;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
//这是客户端，在监听9999，等待客户端的连接，并保持通信
public class javaServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果这些用户登录，就认为是合法的
    //使用ConcurrentHashMap，可以处理并发的集合，没有线程安全
    // HashMap没有处理线程安全，因此在多线程安全下是不安全的
    //ConcurrentHashMap处理的线程安全，即线程同步处理，在多线程情况下是安全的
    private static ConcurrentHashMap<String,User> validUsers =new ConcurrentHashMap<>();
    static {//在静态代码块初始化 validUsers
        validUsers.put("100",new User("100","123456",1));
        validUsers.put("200",new User("200","123456",1));
        validUsers.put("300",new User("300","123456",1));
        validUsers.put("至尊宝",new User("至尊宝","123456",1));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456",1));
        validUsers.put("菩提老祖",new User("菩提老祖","123456",1));
    }
    //验证用户是否合法
    private boolean checkUser(String userId,String passwd){
            User user = validUsers.get(userId);
            if (user == null) {//用户不存在
                return false;
            }
            return user.getPasswd().equals(passwd);
    }
    public boolean checkSign(String userId,String passwd){
            User user = validUsers.get(userId);
            if (user == null) {//用户不存在
                validUsers.put(userId, new User(userId, passwd,1));//添加该用户到validUsers集合
                //HistoricalMessage.AddLinkListToHashMap(userId);//创建添加消息链表到HisMess集合
                return true;
            }
        return false;//用户名已经存在
    }
    public javaServer() {

            try {
                System.out.println("服务端在9999端口监听...");
                ss = new ServerSocket(9999);
                while (true) {//当和某个客户端连接后，会继续监听
                    Socket socket = ss.accept();//如果没有客户端连接，就会阻塞在这里
                    //得到socket关联的对象输入流
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    //得到socket关联的对象的输出流
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    User u = (User) ois.readObject();//读取客户端发送的User对象
                    //创建一个Message对象，准备回复客户端
                    Message message = new Message();
                    if(u.getUsertype()==0) {//如果是将要注册的用户就进入注册方法
                        if (checkSign(u.getUserid(), u.getPasswd())) {
                            message.setMesType(MessageType.MESSAGE_SIGN_UP_SUCCEED);
                            oos.writeObject(message);
                            System.out.println(u.getUserid()+"注册成功");


                        } else {
                            message.setMesType(MessageType.MESSAGE_SIGN_UP_FAIL);
                            oos.writeObject(message);
                        }
                    }else {//否则进入检查用户方法
                        if (checkUser(u.getUserid(), u.getPasswd())) {//合法用户，登录成功
                            System.out.println(u.getUserid()+"登录成功");
                            message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                            //将message对象回复给客户端
                            oos.writeObject(message);
                            //创建一个线程，和客户端保持通信,该线程应该持有socket对象
                            ServerConnectClientThread serverConnectClientThread =
                                    new ServerConnectClientThread(socket, u.getUserid());
                            //启动该线程
                            serverConnectClientThread.start();
                            //把该线程对象，放入一个集合中，进行管理
                            ManageClientTreads.addClientThread(u.getUserid(), serverConnectClientThread);
                            //用户登录提醒功能
                            HashMap<String,ServerConnectClientThread> hm=ManageClientTreads.getHm();
                            Iterator<String> iterator=hm.keySet().iterator();
                            while(iterator.hasNext()){
                                String OnlineUserID =iterator.next().toString();
                                if(!OnlineUserID.equals(u.getUserid())) {
                                    ObjectOutputStream oos1 =
                                            new ObjectOutputStream(hm.get(OnlineUserID).getSocket().getOutputStream());
                                    Message message4 = new Message();
                                    message4.setSender(u.getUserid());
                                    message4.setMesType(MessageType.USER_ONLINE);
                                    message4.setGetter(OnlineUserID);
                                    oos1.writeObject(message4);
                                }
                            }
                        } else {//登录失败
                            System.out.println("用户 id=" + u.getUserid() + " pwd=" + u.getPasswd() + " 验证失败");
                            message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                            oos.writeObject(message);
                            //关闭socket
                            socket.close();
                        }
                    }
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                //如果服务端推出了while循环，说明服务器端不在监听，因此需要关闭ServerSocket
                try {
                    ss.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

