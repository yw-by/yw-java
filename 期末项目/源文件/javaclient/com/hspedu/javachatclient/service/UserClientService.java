package com.hspedu.javachatclient.service;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import com.hspedu.javachatcommon.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
//��������û���¼��֤���û�ע��ȹ���
public class UserClientService {
    private User u=new User();
    private Socket socket;

    public boolean SignUp(String userID, String pwd){
        boolean b= false;
        //����һ��User����
        u.setUserid(userID);
        u.setPasswd(pwd);
        u.setUsertype(0);
        try {
            //���ӷ���ˣ�����u����
            Socket socket= new Socket(InetAddress.getByName("127.0.0.1"),9999);
            //�õ�ObjectOutputStream����
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//����User����

            //��ȡ�ӷ���˻ظ���Message����
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            Message ms =(Message) ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_SIGN_UP_SUCCEED)){//ע��ɹ�
                b=true;
                System.out.println("ע��ɹ�");

            }else{
                //�����¼ʧ�ܣ�ū�ܴ����̣߳��ر�socket
                System.out.println("ע��ʧ�ܣ����û����Ѵ���");


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;

    }
    //����UserID��pwd ����������֤���û��Ƿ�Ϸ�
    public boolean checkUser(String userID,String pwd){
        boolean b=false;
    //����һ��User����
        u.setUserid(userID);
        u.setPasswd(pwd);
        u.setUsertype(1);

        try {
            //���ӷ���ˣ�����u����
            Socket socket= new Socket(InetAddress.getByName("127.0.0.1"),9999);
            //�õ�ObjectOutputStream����
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//����User����

            //��ȡ�ӷ���˻ظ���Message����
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            Message ms =(Message) ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){//��¼�ɹ�
                b=true;
                //����һ���ͷ������˱���ͨ�ŵ��߳�-> ����һ���� ClientConnectServerThread

                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //�����ͻ��˵��߳�
                clientConnectServerThread.start();
                //Ϊ�˺���ͻ��˵���չ�����ǽ����Ϸ��뼯����
                ManageClientConnectServerThread.addClientConnectServerThread(userID,clientConnectServerThread);

                }else{
                //�����¼ʧ�ܣ����ܴ����̣߳��ر�socket
                socket.close();

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    //������������������û��б�
    public void onlineFriendList(){
        Message message =new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserid());
        //���͸�������

        try {
            //���̼߳��ϸ���uidȡ����Ӧ�߳�
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserid());
            //�Ӷ�Ӧ�߳���ȡ��socket
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());//��socketȡ�������
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //��д�����˳��ͻ��ˣ���������˷���һ���˳�ϵͳ��message
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserid());//һ��Ҫָ�����ĸ��ͻ���id
        //����message
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserid()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserid()+"�˳�ϵͳ");
            System.exit(0);//�˳�ϵͳ
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
