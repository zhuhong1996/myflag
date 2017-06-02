package com.example.sdu.myflag.bean;

import java.io.Serializable;

/**
 * 监督用户的bean
 */
public class SuperViseBriefBean implements Serializable{
    public String uid;
    public String nickname;
    public String achieve;
    public String evaluate;
    public int iconId;

    public SuperViseBriefBean(String uid, String nickname, String achieve, String evaluate, int iconId){
        this.uid = uid;
        this.nickname = nickname;
        this.achieve = achieve;
        this.evaluate = evaluate;
        this.iconId = iconId;
    }
}
