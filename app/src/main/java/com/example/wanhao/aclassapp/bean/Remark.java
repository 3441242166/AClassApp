package com.example.wanhao.aclassapp.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhao on 2018/3/28.
 */

public class Remark implements MultiItemEntity {

    public static final int NORMAL = 0;
    public static final int SPECIAL = 1;
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private String date;
    @SerializedName("reply")
    private int reply;
    @SerializedName("user")
    private User user;

    private int userHead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public User getUserNmae() {
        return user;
    }

    public void setUserNmae(User user) {
        this.user = user;
    }

    public int getUserHead() {
        return userHead;
    }

    public void setUserHead(int userHead) {
        this.userHead = userHead;
    }

    @Override
    public int getItemType() {
        return reply==-1?NORMAL:SPECIAL;
    }
}
