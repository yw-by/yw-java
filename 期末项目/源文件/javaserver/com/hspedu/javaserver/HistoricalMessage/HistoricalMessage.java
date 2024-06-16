package com.hspedu.javaserver.HistoricalMessage;
import com.hspedu.javachatcommon.Message;
import com.hspedu.javachatcommon.MessageType;
import java.util.concurrent.ConcurrentHashMap;
//该类用于管理各用户的历史信息链表
public class HistoricalMessage {
    private static ConcurrentHashMap<String,Node> HisMess =new ConcurrentHashMap<>();
    public static void AddLinkListToHashMap(String userId){
        HisMess.put(userId, new Node(userId,null));//添加新用户的消息表头到集合中
    }
    public static Node getLinkList(String userID){
        return HisMess.get(userID);
    }
    public static void AddNodeToLinkList(String userID,Message message){//能进入第二级菜单的用户肯定是有对应链表的，所以这里不写检测
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
            //遍历对应链表

            while (node.next != null) {
                node = node.next;
                if (node.message.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {//对于群发的消息
                    HistoricalMessage += node.message.getSender().toString() + "在" + node.message.getSendTime().toString() + "给所有人" + "发送" + node.message.getContent().toString() + "/ff";
                } else {
                    HistoricalMessage += node.message.getSender().toString() + "在" + node.message.getSendTime().toString() + "给" + node.message.getGetter().toString() + "发送" + node.message.getContent().toString() + "/ff";
                }
            }
        }
        return HistoricalMessage;
    }
}

