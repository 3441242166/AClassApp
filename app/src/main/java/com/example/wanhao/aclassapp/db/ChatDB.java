package com.example.wanhao.aclassapp.db;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.util.DateUtil;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ChatDB extends RealmObject implements MultiItemEntity,Serializable {
    public static final int USER_ME = 0;
    public static final int USER_OTHER = 1;
    public static final int USER_TEACHER = 2;

    @PrimaryKey
    private String chatID;
    private String courseID;
    private String userID;

    private String content;
    private String date;
    private String messageType;

    private  UserDB user;

    private int itemType;

    public ChatDB() {
    }

    public ChatDB(ChatBean bean) {
        chatID = bean.getId();
        courseID = bean.getCourseID();
        userID = bean.getUser().getId();

        content = bean.getContent();
        date = bean.getDate();
        messageType = bean.getMessageType();

        user = new UserDB(bean.getUser());
    }


    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public UserDB getUser() {
        return user;
    }

    public void setUser(UserDB user) {
        this.user = user;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
