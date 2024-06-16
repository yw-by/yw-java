package com.hspedu.javaserver.HistoricalMessage;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import java.util.concurrent.ConcurrentHashMap;
//�������ڹ�����û�����ʷ��Ϣ����
public class HistoricalMessage {
    private static ConcurrentHashMap<String,Node> HisMess =new ConcurrentHashMap<>();
    public static void AddLinkListToHashMap(String userId){
        HisMess.put(userId, new Node(userId,null));//������û�����Ϣ��ͷ��������
    }
    public static Node getLinkList(String userID){
        return HisMess.get(userID);
    }
    public static void AddNodeToLinkList(String userID,Message message){//�ܽ���ڶ����˵����û��϶����ж�Ӧ����ģ��������ﲻд���
        Node node=HisMess.get(userID);
        if(node==null) {
            HistoricalMessage.AddLinkListToHashMap(userID);
            node = HisMess.get(userID);
        }
        while (node.next != null) {
            node = node.next;
        }
        node.next = new Node(userID, message, null);
    }
    public static String getHisMess(Node node){
        String HistoricalMessage = "";
        if(node!=null  && node.next!=null) {
            //������Ӧ����

            while (node.next != null) {
                node = node.next;
                if (node.message.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {//����Ⱥ������Ϣ
                    HistoricalMessage += node.message.getSender().toString() + "��" + node.message.getSendTime().toString() + "��������" + "����" + node.message.getContent().toString() + "/ff";
                } else {
                    HistoricalMessage += node.message.getSender().toString() + "��" + node.message.getSendTime().toString() + "��" + node.message.getGetter().toString() + "����" + node.message.getContent().toString() + "/ff";
                }
            }
        }
        return HistoricalMessage;
    }
}

