package com.example.sdu.myflag.bean;

import java.io.Serializable;

/**
 * 申请监督的bean
 */
public class ApplySuperviseBean implements Serializable{
    public String fid; // flag的id
    public String agree; // 是否同意（0代表未确定  1代表同意 2代表不同意）
    public String applyUid; // 申请者的id
    public String nickname; // 申请者昵称
    public int iconId;
}
