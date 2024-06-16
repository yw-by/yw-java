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
//���ǿͻ��ˣ��ڼ���9999���ȴ��ͻ��˵����ӣ�������ͨ��
public class javaServer {
    private ServerSocket ss = null;
    //����һ�����ϣ���Ŷ���û��������Щ�û���¼������Ϊ�ǺϷ���
    //ʹ��ConcurrentHashMap�����Դ������ļ��ϣ�û���̰߳�ȫ
    // HashMapû�д����̰߳�ȫ������ڶ��̰߳�ȫ���ǲ���ȫ��
    //ConcurrentHashMap������̰߳�ȫ�����߳�ͬ�������ڶ��߳�������ǰ�ȫ��
    private static ConcurrentHashMap<String,User> validUsers =new ConcurrentHashMap<>();
    static {//�ھ�̬������ʼ�� validUsers
        validUsers.put("100",new User("100","123456",1));
        validUsers.put("200",new User("200","123456",1));
        validUsers.put("300",new User("300","123456",1));
        validUsers.put("����",new User("����","123456",1));
        validUsers.put("��ϼ����",new User("��ϼ����","123456",1));
        validUsers.put("��������",new User("��������","123456",1));
    }
    //��֤�û��Ƿ�Ϸ�
    private boolean checkUser(String userId,String passwd){
            User user = validUsers.get(userId);
            if (user == null) {//�û�������
                return false;
            }
            return user.getPasswd().equals(passwd);
    }
    public boolean checkSign(String userId,String passwd){
            User user = validUsers.get(userId);
            if (user == null) {//�û�������
                validUsers.put(userId, new User(userId, passwd,1));//��Ӹ��û���validUsers����
                //HistoricalMessage.AddLinkListToHashMap(userId);//���������Ϣ����HisMess����
                return true;
            }
        return false;//�û����Ѿ�����
    }
    public javaServer() {

            try {
                System.out.println("�������9999�˿ڼ���...");
                ss = new ServerSocket(9999);
                while (true) {//����ĳ���ͻ������Ӻ󣬻��������
                    Socket socket = ss.accept();//���û�пͻ������ӣ��ͻ�����������
                    //�õ�socket�����Ķ���������
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    //�õ�socket�����Ķ���������
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    User u = (User) ois.readObject();//��ȡ�ͻ��˷��͵�User����
                    //����һ��Message����׼���ظ��ͻ���
                    Message message = new Message();
                    if(u.getUsertype()==0) {//����ǽ�Ҫע����û��ͽ���ע�᷽��
                        if (checkSign(u.getUserid(), u.getPasswd())) {
                            message.setMesType(MessageType.MESSAGE_SIGN_UP_SUCCEED);
                            oos.writeObject(message);
                            System.out.println(u.getUserid()+"ע��ɹ�");


                        } else {
                            message.setMesType(MessageType.MESSAGE_SIGN_UP_FAIL);
                            oos.writeObject(message);
                        }
                    }else {//����������û�����
                        if (checkUser(u.getUserid(), u.getPasswd())) {//�Ϸ��û�����¼�ɹ�
                            System.out.println(u.getUserid()+"��¼�ɹ�");
                            message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                            //��message����ظ����ͻ���
                            oos.writeObject(message);
                            //����һ���̣߳��Ϳͻ��˱���ͨ��,���߳�Ӧ�ó���socket����
                            ServerConnectClientThread serverConnectClientThread =
                                    new ServerConnectClientThread(socket, u.getUserid());
                            //�������߳�
                            serverConnectClientThread.start();
                            //�Ѹ��̶߳��󣬷���һ�������У����й���
                            ManageClientTreads.addClientThread(u.getUserid(), serverConnectClientThread);
                            //�û���¼���ѹ���
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
                        } else {//��¼ʧ��
                            System.out.println("�û� id=" + u.getUserid() + " pwd=" + u.getPasswd() + " ��֤ʧ��");
                            message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                            oos.writeObject(message);
                            //�ر�socket
                            socket.close();
                        }
                    }
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                //���������Ƴ���whileѭ����˵���������˲��ڼ����������Ҫ�ر�ServerSocket
                try {
                    ss.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

