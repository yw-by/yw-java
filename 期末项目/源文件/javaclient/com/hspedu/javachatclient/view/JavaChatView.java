package com.hspedu.javachatclient.view;
import com.hspedu.javachatclient.service.MessageClientService;
import com.hspedu.javachatclient.service.UserClientService;
import com.hspedu.javachatclient.utils.Utility;
//客户端菜单界面
public class JavaChatView {
    private boolean loop =true;//控制是否显示菜单
    private String key="";//接收用户的键盘输入
    private UserClientService userClientService =new UserClientService();//用于登陆服务/用户注册
    private MessageClientService messageClientService=new MessageClientService();//用于私聊/群聊/查看历史记录
    //显示主菜单
    public static void main(String[] args) {
        new JavaChatView().mainMenu();
        System.out.println("客户端退出系统。。。。。");
    }
    public void mainMenu(){
        while (loop){
            System.out.println("==========欢迎登录网络通信系统==========");
            System.out.println("\t\t 1 登录系统");//\t是制表符
            System.out.println("\t\t 2 用户注册");
            System.out.println("\t\t 9 退出系统");
            System.out.print("输入选择：");
            key= Utility.readString(1);//Utility为自定义工具包，这行意为从键盘输入一个数字
            //根据用户的输入，来处理不同的逻辑
            switch (key){
                case "1":
                    System.out.print("请输入用户ID：");
                    String userID= Utility.readString(50);
                    System.out.print("请输入密码：");
                    String pwd=Utility.readString(50);
                    //这里之后就需要去服务端验证该用户是否合法
                    if(userClientService.checkUser(userID,pwd)){
                        System.out.println("==========欢迎 (用户 "+userID+" 登录成功) ==========");
                        //进入二级菜单
                        while (loop) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("\n==========网络通信系统二级菜单(用户 "+userID+" )=============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 查看历史消息");
                            System.out.println("\t\t 5 傻瓜机器人");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("输入选择：");
                            key=Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("请输入你要群发的消息：");
                                    String content2 = Utility.readString(100);
                                    messageClientService.SendMessageToAll(content2,userID);
                                    break;
                                case "3":
                                    System.out.print("请输入想要发送消息的用户id（在线）：");
                                    String getter = Utility.readString(50);
                                    System.out.print("请输入想要发说的话： ");
                                    String content = Utility.readString(100);
                                    messageClientService.SendMessageToOne(content,userID,getter);
                                    break;
                                case "4":
                                    messageClientService.ViewMessages(userID);
                                    break;
                                case "5":
                                    System.out.println("\t\t 你可以问下面这些问题：（输入数字编号）");
                                    System.out.println("\t\t 1 Java中的基本数据类型有哪些？");
                                    System.out.println("\t\t 2 什么是Java中的包（package）？");
                                    System.out.println("\t\t 3 Java中的访问修饰符有哪些？它们的作用是什么？");
                                    System.out.println("\t\t 4 Java中的继承和接口有什么区别？");
                                    System.out.println("\t\t 5 Java中的异常处理机制是什么？");
                                    String choose=Utility.readString(1);
                                    messageClientService.AskRobot(userID,choose);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop=false;
                                    break;
                            }
                        }
                    }else{//登录服务器失败
                        System.out.println("=========登录失败===============");
                    }
                    break;
                case "2":
                    System.out.print("输入要注册的用户名: ");
                    String userid=Utility.readString(50);
                    System.out.print("请输入注册密码: ");
                    String password=Utility.readString(50);
                    userClientService.SignUp(userid,password);
                    break;
                case "9":
                    loop=false;
                    break;
            }
        }

    }
}
