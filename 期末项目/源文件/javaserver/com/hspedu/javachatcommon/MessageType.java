package com.hspedu.javachatcommon;
//��ʾ��Ϣ����
public interface MessageType {
    //�ýӿڶ�����һЩ����
    //��ͬ�ĳ�����ֵ��ʾ��ͬ����Ϣ����
    String MESSAGE_LOGIN_SUCCEED="1";//��¼�ɹ�
    String MESSAGE_LOGIN_FAIL="2";//��¼ʧ��
    String MESSAGE_COMM_MES="3";//��ͨ��Ϣ
    String MESSAGE_GET_ONLINE_FRIEND="4";//���������û��б�
    String MESSAGE_RET_ONLINE_FRIEND="5";//���������û��б�
    String MESSAGE_CLIENT_EXIT="6";//�ͻ��������˳�
    String MESSAGE_TO_ALL="7";//Ⱥ��
    String MESSAGE_SIGN_UP_SUCCEED="8";//ע��ɹ�
    String MESSAGE_SIGN_UP_FAIL="9";//ע��ʧ��
    String VIEW_HISTORICAL_MESSAGES="10";//������ʷ��Ϣ
    String RET_HISTORICAL_MESSAGES="11";//������ʷ��Ϣ
    String USER_ONLINE="12";//�û�����
    String USER_OFFLINE="13";//�û�����
    String ASK_ROBOT="14";//�����������
    String RET_ROBOT="15";//�����˻ظ�
    String USER_NOT_ONLINE="16";//�����û�������
}
