package com.hspedu.javaserver.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import com.hspedu.javaserver.HistoricalMessage.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
//�����Ӧ�Ķ����ı���ͻ��˱���ͨ��
public class ServerConnectClientThread extends Thread{
     private Socket socket;
     private String userID;//���ӵ��������˵��û�id

    public ServerConnectClientThread(Socket socket,String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {//�����̴߳���run״̬�����Է���/������Ϣ
        while(true){
            try {
                //System.out.println("����˺Ϳͻ���"+userID+"����ͨ��,��ȡ���ݡ�����");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message=(Message) ois.readObject();
                //����message�����ͣ�����Ӧ��ҵ����
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(message.getSender()+"���������û��б�");
                    String onlineUsers = ManageClientTreads.getOnlineUsers();
                    //����message
                    //����һ��Message���󣬷��ظ��ͻ���
                    Message message2=new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUsers);
                    message2.setGetter(message.getSender());
                    //���ظ��ͻ���
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){

                    System.out.println(message.getSender()+"�������˷�����Ϣ");
                    //����hash��ת�������˷����ߵ����������û�
                    HashMap<String,ServerConnectClientThread> hm=ManageClientTreads.getHm();
                    Iterator<String> iterator=hm.keySet().iterator();
                    //�����ߴ洢��ʷ��Ϣ
                    HistoricalMessage.AddNodeToLinkList(message.getSender(),message);
                    while(iterator.hasNext()){
                        String OnlineUserID =iterator.next().toString();
                        if(!OnlineUserID.equals(message.getSender())){//���˷�����
                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(OnlineUserID).getSocket().getOutputStream());
                            oos.writeObject(message);
                            //ÿһ�������ߵ���ʷ��ϢҲҪ��¼
                            HistoricalMessage.AddNodeToLinkList(OnlineUserID,message);
                        }
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    if(ManageClientTreads.CheckUserIsOnline(message.getGetter())) {
                        System.out.println(message.getSender() + "��" + message.getGetter() + "������Ϣ");
                        //����message��ȡgetter id���ٸ���id��ȡ��Ӧ�߳�
                        ServerConnectClientThread serverConnectClientThread = ManageClientTreads.getServerConnectClientThread(message.getGetter());
                        //��ȡsocket�����������messageת������Ӧ�û�
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                        //�ֱ�������ߺͽ����ߵ���ʷ��Ϣ���������һ��¼
                        HistoricalMessage.AddNodeToLinkList(message.getSender(), message);
                        HistoricalMessage.AddNodeToLinkList(message.getGetter(), message);
                    }else{
                        ServerConnectClientThread serverConnectClientThread = ManageClientTreads.getServerConnectClientThread(message.getSender());
                        //��ȡsocket�����������messageת������Ӧ�û�
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        Message message7 = new Message();
                        message7.setMesType(MessageType.USER_NOT_ONLINE);
                        message7.setGetter(message.getSender());
                        oos.writeObject(message7);
                    }
                }else if(message.getMesType().equals(MessageType.VIEW_HISTORICAL_MESSAGES)){//������ʷ��¼
                    System.out.println(message.getSender()+"������ʷ��¼");
                    //���̹߳������л�ȡ������ʷ��Ϣ��¼�ߵ��߳�
                    ServerConnectClientThread serverConnectClientThread = ManageClientTreads.getServerConnectClientThread(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    Message message3=new Message();
                    message3.setGetter(message.getSender());
                    message3.setMesType(MessageType.RET_HISTORICAL_MESSAGES);
                    message3.setContent(HistoricalMessage.getHisMess(HistoricalMessage.getLinkList(message.getSender())));
                    oos.writeObject(message3);
                }else if(message.getMesType().equals(MessageType.ASK_ROBOT)){
                    System.out.println(message.getSender()+"����"+message.getContent());
                    Message message6 = new Message();
                    message6.setGetter(message.getSender());
                    message6.setMesType(MessageType.RET_ROBOT);
                    ObjectOutputStream oos =
                            new ObjectOutputStream(ManageClientTreads.getServerConnectClientThread(message.getSender()).getSocket().getOutputStream());
                    switch (message.getContent()){
                        case "1":
                            message6.setContent("Java�еĻ����������Ͱ�����byte��short��int��long��float��double��char��boolean��");
                            oos.writeObject(message6);
                            break;
                        case "2":
                            message6.setContent("����package����������֯�͹���Java���һ�ֻ��ơ������Խ���ص�����֯��һ���Ա���õع����ά�����롣��ͨ�����������ռ������ֹ������ͻ�����ṩ���õĴ�����֯�ṹ��");
                            oos.writeObject(message6);
                            break;
                            case "3":
                            message6.setContent("Java�еķ������η�����public��protected��default����д�κ����η�����private���������������ࡢ�����ͱ����ķ���Ȩ�ޡ�������˵��\n" +
                                    "\n" +
                                    "- public�����Ա��κ�����ʡ�\n" +
                                    "- protected�����Ա�ͬһ�����е���͸����������ʡ�\n" +
                                    "- default����д�κ����η��������Ա�ͬһ�����е�����ʡ�\n" +
                                    "- private��ֻ�ܱ�ͬһ�����е������������ʡ�");
                            oos.writeObject(message6);
                            break;
                        case "4":
                            message6.setContent("�̳У�inheritance���ͽӿڣ�interface������Java��ʵ�ִ������úͶ�̬�ԵĻ��ƣ���������һЩ����\n" +
                                    "\n" +
                                    "- �̳У�ͨ���̳У�һ������Ի����һ��������Ժͷ�����������Լ̳и���ķ�˽�����Ժͷ��������ҿ��Ը��ǣ�override������ķ�����\n" +
                                    "- �ӿڣ��ӿڶ�����һ����󷽷���ʵ�ֽӿڵ�������ṩ��Щ�����ľ���ʵ�֡�һ�������ʵ�ֶ���ӿڣ��Ӷ��ﵽ��̳е�Ч����");
                            oos.writeObject(message6);
                            break;
                        case "5":
                            message6.setContent("Java�е��쳣�������ͨ��try-catch�����ʵ�֡������ܷ����쳣�Ĵ�������try����ʱ������ʹ��catch�鲶�񲢴�������׳����쳣�����⣬������ʹ��finally����ִ�������Ƿ����쳣����Ҫִ�еĴ��룬������Դ�ͷŵȡ�");
                            oos.writeObject(message6);
                            break;
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){//�ͻ����˳�
                    System.out.println(message.getSender()+" �˳�");
                    //�û��˳����ѹ���
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
                    socket.close();//�ر��߳�
                    break;//�˳�ѭ��
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}

