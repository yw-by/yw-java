package com.hspedu.javachatclient.view;
import com.hspedu.javachatclient.service.MessageClientService;
import com.hspedu.javachatclient.service.UserClientService;
import com.hspedu.javachatclient.utils.Utility;
//�ͻ��˲˵�����
public class JavaChatView {
    private boolean loop =true;//�����Ƿ���ʾ�˵�
    private String key="";//�����û��ļ�������
    private UserClientService userClientService =new UserClientService();//���ڵ�½����/�û�ע��
    private MessageClientService messageClientService=new MessageClientService();//����˽��/Ⱥ��/�鿴��ʷ��¼
    //��ʾ���˵�
    public static void main(String[] args) {
        new JavaChatView().mainMenu();
        System.out.println("�ͻ����˳�ϵͳ����������");
    }
    public void mainMenu(){
        while (loop){
            System.out.println("==========��ӭ��¼����ͨ��ϵͳ==========");
            System.out.println("\t\t 1 ��¼ϵͳ");//\t���Ʊ��
            System.out.println("\t\t 2 �û�ע��");
            System.out.println("\t\t 9 �˳�ϵͳ");
            System.out.print("����ѡ��");
            key= Utility.readString(1);//UtilityΪ�Զ��幤�߰���������Ϊ�Ӽ�������һ������
            //�����û������룬������ͬ���߼�
            switch (key){
                case "1":
                    System.out.print("�������û�ID��");
                    String userID= Utility.readString(50);
                    System.out.print("���������룺");
                    String pwd=Utility.readString(50);
                    //����֮�����Ҫȥ�������֤���û��Ƿ�Ϸ�
                    if(userClientService.checkUser(userID,pwd)){
                        System.out.println("==========��ӭ (�û� "+userID+" ��¼�ɹ�) ==========");
                        //��������˵�
                        while (loop) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("\n==========����ͨ��ϵͳ�����˵�(�û� "+userID+" )=============");
                            System.out.println("\t\t 1 ��ʾ�����û��б�");
                            System.out.println("\t\t 2 Ⱥ����Ϣ");
                            System.out.println("\t\t 3 ˽����Ϣ");
                            System.out.println("\t\t 4 �鿴��ʷ��Ϣ");
                            System.out.println("\t\t 5 ɵ�ϻ�����");
                            System.out.println("\t\t 9 �˳�ϵͳ");
                            System.out.print("����ѡ��");
                            key=Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("��������ҪȺ������Ϣ��");
                                    String content2 = Utility.readString(100);
                                    messageClientService.SendMessageToAll(content2,userID);
                                    break;
                                case "3":
                                    System.out.print("��������Ҫ������Ϣ���û�id�����ߣ���");
                                    String getter = Utility.readString(50);
                                    System.out.print("��������Ҫ��˵�Ļ��� ");
                                    String content = Utility.readString(100);
                                    messageClientService.SendMessageToOne(content,userID,getter);
                                    break;
                                case "4":
                                    messageClientService.ViewMessages(userID);
                                    break;
                                case "5":
                                    System.out.println("\t\t �������������Щ���⣺���������ֱ�ţ�");
                                    System.out.println("\t\t 1 Java�еĻ���������������Щ��");
                                    System.out.println("\t\t 2 ʲô��Java�еİ���package����");
                                    System.out.println("\t\t 3 Java�еķ������η�����Щ�����ǵ�������ʲô��");
                                    System.out.println("\t\t 4 Java�еļ̳кͽӿ���ʲô����");
                                    System.out.println("\t\t 5 Java�е��쳣���������ʲô��");
                                    String choose=Utility.readString(1);
                                    messageClientService.AskRobot(userID,choose);
                                    break;
                                case "9":
                                    //���÷�����������������һ���˳�ϵͳ��message
                                    userClientService.logout();
                                    loop=false;
                                    break;
                            }
                        }
                    }else{//��¼������ʧ��
                        System.out.println("=========��¼ʧ��===============");
                    }
                    break;
                case "2":
                    System.out.print("����Ҫע����û���: ");
                    String userid=Utility.readString(50);
                    System.out.print("������ע������: ");
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
