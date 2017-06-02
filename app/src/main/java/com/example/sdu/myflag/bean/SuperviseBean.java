package com.example.sdu.myflag.bean;

import java.io.Serializable;

/**
 * 监督的bean
 */
public class SuperviseBean implements Serializable{
    public String fid;
    public String agree;
    public String uid;
    public FlagBean flagBean;
    public String nickname;
    public String sex;
    public int iconId;

    public SuperviseBean(){}

    public SuperviseBean(String fid, String agree, String uid){
        this.fid = fid;
        this.agree = agree;
        this.uid = uid;
        this.flagBean = new FlagBean();
    }
}
