package com.example.wanhao.aclassapp.bean;

import android.content.Intent;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.wanhao.aclassapp.bean.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by wanhao on 2018/4/5.
 */

public class ChatBean implements MultiItemEntity ,Serializable{
    public static final int ME = 1;
    public static final int OTHER = 2;

    @SerializedName("id")
    String id;
    @SerializedName("content")
    String content;
    @SerializedName("date")
    String date;
    @SerializedName("messageType")
    String messageType;
    @SerializedName("courseId")
    String courseID;
    @SerializedName("user")
    User user;


    int type;

    public ChatBean(){
        user = new User();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItemType(int type) {
        this.type = type;
    }
    @Override
    public int getItemType() {
        return type;
    }
}
