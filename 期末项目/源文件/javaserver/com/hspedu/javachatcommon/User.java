package com.hspedu.javachatcommon;

import java.io.Serializable;

//��ʾһ���û�/�ͻ���Ϣ
public class User implements Serializable {//����ӿ������л���������ת��Ϊ�ֽ������Ա㽫�䱣�浽�ļ��л�ͨ�����紫��
    private static final long serialVersionUID=1L;//���������ǿ������
    private String userid;//�û�ID/�û���

    private String passwd;//�û�����
    public int usertype;//����ע��,0��ʾ�������ǽ�Ҫע����û���1��ʾ�������Ѿ�ע����û�
    public User(String userid, String passwd,int e){
        this.userid=userid;
        this.passwd=passwd;
        this.usertype=e;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public int getUsertype(){return usertype;}



}
