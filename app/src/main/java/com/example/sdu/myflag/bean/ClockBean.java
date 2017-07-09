package com.example.sdu.myflag.bean;

import java.io.PushbackInputStream;

/**
 * 打卡实体
 */
public class ClockBean {
    public String cid; // 卡的id
    public String uid; // 用户的id
    public String fid; // flag的id
    public String content; // 内容
    public String time; // 时间
    public String photo; // 图片名
    public int iconId; // 头像id
    public String nickname; // 用户名

    public ClockBean(String cid, String uid, String fid, String content, String time, String photo, int iconId, String nickname){
        this.cid = cid;
        this.uid = uid;
        this.fid = fid;
        this.content = content;
        this.time = time;
        this.photo = photo;
        this.iconId = iconId;
        this.nickname = nickname;
    }
}
