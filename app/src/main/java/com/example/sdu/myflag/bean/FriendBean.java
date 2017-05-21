package com.example.sdu.myflag.bean;

import java.io.Serializable;

/**
 * 字母实体
 */
public class FriendBean implements Comparable<FriendBean>, Serializable{

    private String id;
    private String name;//名字
    private String remark;
    private String firstLetter;//名字首字母
    int iconId;

    public FriendBean() {
        super();
    }

    public FriendBean(String id, String name, String firstLetter, String remark, int iconId) {
        super();
        this.id = id;
        this.name = name;
        this.firstLetter = firstLetter;
        this.remark = remark;
        this.iconId = iconId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    @Override
    public int compareTo(FriendBean another) {
        if (this.getFirstLetter().equals("@")
                || another.getFirstLetter().equals("#")) {
            return -1;
        } else if (this.getFirstLetter().equals("#")
                || another.getFirstLetter().equals("@")) {
            return 1;
        } else {
            return this.getFirstLetter().compareTo(another.getFirstLetter());
        }
    }

}
