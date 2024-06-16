package com.hspedu.javachatcommon;

import java.io.Serializable;

//表示一个用户/客户信息
public class User implements Serializable {//这个接口是序列化，将对象转换为字节流，以便将其保存到文件中或通过网络传输
    private static final long serialVersionUID=1L;//这个可以增强兼容性
    private String userid;//用户ID/用户名

    private String passwd;//用户密码
    public int usertype;//用于注册,0表示不存在是将要注册的用户，1表示存在是已经注册的用户
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
