package com.example.sdu.myflag.bean;

import java.io.Serializable;

/**
 * 添加好友信息bean
 */
public class TempFriendBean implements Serializable{
    public String nickname;
    public String phone;
    public String message;
    public String requestUid;
    public String agree;
    public int iconId;

    public TempFriendBean(){}

    public TempFriendBean(String nickname, String phone, String message, String requestUid, String agree, int iconId){
        this.nickname = nickname;
        this.phone = phone;
        this.message = message;
        this.requestUid = requestUid;
        this.agree = agree;
        this.iconId = iconId;
    }
}
