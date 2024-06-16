package com.hspedu.javachatcommon;
//表示消息类型
public interface MessageType {
    //该接口定义了一些常量
    //不同的常量的值表示不同的消息类型
    String MESSAGE_LOGIN_SUCCEED="1";//登录成功
    String MESSAGE_LOGIN_FAIL="2";//登录失败
    String MESSAGE_COMM_MES="3";//普通信息
    String MESSAGE_GET_ONLINE_FRIEND="4";//请求在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND="5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT="6";//客户端请求退出
    String MESSAGE_TO_ALL="7";//群发
    String MESSAGE_SIGN_UP_SUCCEED="8";//注册成功
    String MESSAGE_SIGN_UP_FAIL="9";//注册失败
    String VIEW_HISTORICAL_MESSAGES="10";//请求历史消息
    String RET_HISTORICAL_MESSAGES="11";//返回历史消息
    String USER_ONLINE="12";//用户上线
    String USER_OFFLINE="13";//用户下线
    String ASK_ROBOT="14";//向机器人提问
    String RET_ROBOT="15";//机器人回复
    String USER_NOT_ONLINE="16";//所发用户不在线
}
