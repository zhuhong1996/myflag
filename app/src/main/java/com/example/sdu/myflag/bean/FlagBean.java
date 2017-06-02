package com.example.sdu.myflag.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * flag的bean
 */
public class FlagBean implements Serializable {
    private String fid;
    private String user_name; // 用户名
    private String flag; // flag内容
    private String time_begin; // 起始时间
    private String time_end; // 终止时间
    private ArrayList<String> watch_name; // 监督人姓名
    private String reward; // 奖励
    private String sex; // 性别
    private String teamOrNot;
    private String isFinish; // flag是否完成
    private ArrayList<String> member;
    private ArrayList<SuperViseBriefBean> SuperViseBriefList;
    private String achieve;
    public boolean isSupervise;
    public int iconId;

    public FlagBean(String fid, String user_name, String flag, String time_begin, String time_end, ArrayList<String> watch_name, ArrayList<String> member, String reward, String teamOrNot, String isFinish, ArrayList<SuperViseBriefBean> SuperViseBriefList, String achieve) {
        this.fid = fid;
        this.user_name = user_name;
        this.flag = flag;
        this.time_begin = time_begin;
        this.member = member;
        this.time_end = time_end;
        this.watch_name = watch_name;
        this.reward = reward;
        this.teamOrNot = teamOrNot;
        this.isFinish = isFinish;
        this.SuperViseBriefList = SuperViseBriefList;
        this.achieve = achieve;
    }

    public FlagBean() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTime_begin() {
        return time_begin;
    }

    public void setTime_begin(String time_begin) {
        this.time_begin = time_begin;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<String> getWatch_name() {
        return watch_name;
    }

    public void setWatch_name(ArrayList<String> watch_name) {
        this.watch_name = watch_name;
    }

    public String getTeamOrNot() {
        return teamOrNot;
    }

    public void setTeamOrNot(String teamOrNot) {
        this.teamOrNot = teamOrNot;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public ArrayList<String> getMember() {
        return member;
    }

    public void setMember(ArrayList<String> member) {
        this.member = member;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public ArrayList<SuperViseBriefBean> getSuperViseBriefList() {
        return SuperViseBriefList;
    }

    public void setSuperViseBriefList(ArrayList<SuperViseBriefBean> superViseBriefList) {
        SuperViseBriefList = superViseBriefList;
    }

    public String getAchieve() {
        return achieve;
    }

    public void setAchieve(String achieve) {
        this.achieve = achieve;
    }

    public void setIsSupervise(boolean isSupervise)
    {
        this.isSupervise = isSupervise;
    }

    public boolean getIsSupervise()
    {
        return isSupervise;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }
}
