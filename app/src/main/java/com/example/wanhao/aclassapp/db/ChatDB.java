package com.example.wanhao.aclassapp.db;

import com.example.wanhao.aclassapp.util.DateUtil;

import io.realm.RealmObject;

public class ChatDB extends RealmObject {

    int chatID;
    String content;
    String date;
    long dateNum;
    String type;
    int courseID;
    String userName;
    String userCount;
    String avatar;
    int roleID;


    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
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
        setDateNum(DateUtil.getTimeLongByString(date));
    }

    public long getDateNum() {
        return dateNum;
    }

    public void setDateNum(long dateNum) {
        this.dateNum = dateNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
