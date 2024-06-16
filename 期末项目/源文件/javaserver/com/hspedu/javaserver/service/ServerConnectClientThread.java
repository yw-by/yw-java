package com.hspedu.javaserver.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import com.hspedu.javaserver.HistoricalMessage.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
//该类对应的对象和谋个客户端保持通信
public class ServerConnectClientThread extends Thread{
     private Socket socket;
     private String userID;//连接到服务器端的用户id

    public ServerConnectClientThread(Socket socket,String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {//这里线程处于run状态，可以发送/接收消息
        while(true){
            try {
                //System.out.println("服务端和客户端"+userID+"保持通信,读取数据。。。");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message=(Message) ois.readObject();
                //根据message的类型，做相应的业务处理
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(message.getSender()+"请求在线用户列表");
                    String onlineUsers = ManageClientTreads.getOnlineUsers();
                    //返回message
                    //创建一个Message对象，返回给客户端
                    Message message2=new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUsers);
                    message2.setGetter(message.getSender());
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){

                    System.out.println(message.getSender()+"给所有人发送消息");
                    //遍历hash表，转发给除了发送者的其他在线用户
                    HashMap<String,ServerConnectClientThread> hm=ManageClientTreads.getHm();
                    Iterator<String> iterator=hm.keySet().iterator();
                    //发送者存储历史消息
                    HistoricalMessage.AddNodeToLinkList(message.getSender(),message);
                    while(iterator.hasNext()){
                        String OnlineUserID =iterator.next().toString();
                        if(!OnlineUserID.equals(message.getSender())){//除了发送者
                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(OnlineUserID).getSocket().getOutputStream());
                            oos.writeObject(message);
                            //每一个接收者的历史消息也要记录
                            HistoricalMessage.AddNodeToLinkList(OnlineUserID,message);
                        }
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    if(ManageClientTreads.CheckUserIsOnline(message.getGetter())) {
                        System.out.println(message.getSender() + "给" + message.getGetter() + "发送消息");
                        //根据message获取getter id，再根据id获取对应线程
                        ServerConnectClientThread serverConnectClientThread = ManageClientTreads.getServerConnectClientThread(message.getGetter());
                        //获取socket的输出流，将message转发给对应用户
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                        //分别给发送者和接收者的历史消息链表添加这一记录
                        HistoricalMessage.AddNodeToLinkList(message.getSender(), message);
                        HistoricalMessage.AddNodeToLinkList(message.getGetter(), message);
                    }else{
                        ServerConnectClientThread serverConnectClientThread = ManageClientTreads.getServerConnectClientThread(message.getSender());
                        //获取socket的输出流，将message转发给对应用户
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        Message message7 = new Message();
                        message7.setMesType(MessageType.USER_NOT_ONLINE);
                        message7.setGetter(message.getSender());
                        oos.writeObject(message7);
                    }
                }else if(message.getMesType().equals(MessageType.VIEW_HISTORICAL_MESSAGES)){//请求历史记录
                    System.out.println(message.getSender()+"请求历史记录");
                    //从线程管理集合中获取请求历史消息记录者的线程
                    ServerConnectClientThread serverConnectClientThread = ManageClientTreads.getServerConnectClientThread(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    Message message3=new Message();
                    message3.setGetter(message.getSender());
                    message3.setMesType(MessageType.RET_HISTORICAL_MESSAGES);
                    message3.setContent(HistoricalMessage.getHisMess(HistoricalMessage.getLinkList(message.getSender())));
                    oos.writeObject(message3);
                }else if(message.getMesType().equals(MessageType.ASK_ROBOT)){
                    System.out.println(message.getSender()+"提问"+message.getContent());
                    Message message6 = new Message();
                    message6.setGetter(message.getSender());
                    message6.setMesType(MessageType.RET_ROBOT);
                    ObjectOutputStream oos =
                            new ObjectOutputStream(ManageClientTreads.getServerConnectClientThread(message.getSender()).getSocket().getOutputStream());
                    switch (message.getContent()){
                        case "1":
                            message6.setContent("Java中的基本数据类型包括：byte、short、int、long、float、double、char和boolean。");
                            oos.writeObject(message6);
                            break;
                        case "2":
                            message6.setContent("包（package）是用来组织和管理Java类的一种机制。它可以将相关的类组织在一起，以便更好地管理和维护代码。包通常用于命名空间管理，防止类名冲突，并提供更好的代码组织结构。");
                            oos.writeObject(message6);
                            break;
                            case "3":
                            message6.setContent("Java中的访问修饰符包括public、protected、default（不写任何修饰符）和private。它们用来控制类、方法和变量的访问权限。具体来说：\n" +
                                    "\n" +
                                    "- public：可以被任何类访问。\n" +
                                    "- protected：可以被同一个包中的类和该类的子类访问。\n" +
                                    "- default（不写任何修饰符）：可以被同一个包中的类访问。\n" +
                                    "- private：只能被同一个类中的其他方法访问。");
                            oos.writeObject(message6);
                            break;
                        case "4":
                            message6.setContent("继承（inheritance）和接口（interface）都是Java中实现代码重用和多态性的机制，但它们有一些区别：\n" +
                                    "\n" +
                                    "- 继承：通过继承，一个类可以获得另一个类的属性和方法。子类可以继承父类的非私有属性和方法，并且可以覆盖（override）父类的方法。\n" +
                                    "- 接口：接口定义了一组抽象方法，实现接口的类必须提供这些方法的具体实现。一个类可以实现多个接口，从而达到多继承的效果。");
                            oos.writeObject(message6);
                            break;
                        case "5":
                            message6.setContent("Java中的异常处理机制通过try-catch语句来实现。当可能发生异常的代码块放在try块中时，可以使用catch块捕获并处理可能抛出的异常。另外，还可以使用finally块来执行无论是否发生异常都需要执行的代码，例如资源释放等。");
                            oos.writeObject(message6);
                            break;
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){//客户端退出
                    System.out.println(message.getSender()+" 退出");
                    //用户退出提醒功能
                    HashMap<String,ServerConnectClientThread> hm=ManageClientTreads.getHm();
                    Iterator<String> iterator=hm.keySet().iterator();
                    while(iterator.hasNext()){
                        String OnlineUserID =iterator.next().toString();
                        if(!OnlineUserID.equals(message.getSender())) {
                            ObjectOutputStream oos2 =
                                    new ObjectOutputStream(hm.get(OnlineUserID).getSocket().getOutputStream());
                            Message message5 = new Message();
                            message5.setSender(message.getSender());
                            message5.setMesType(MessageType.USER_OFFLINE);
                            message5.setGetter(OnlineUserID);
                            oos2.writeObject(message5);
                        }
                    }
                    ManageClientTreads.removeServerConnectClientThread(message.getSender());
                    socket.close();//关闭线程
                    break;//退出循环
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}

