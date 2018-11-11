package com.example.wanhao.aclassapp.db;

import com.example.wanhao.aclassapp.bean.User;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserDB extends RealmObject {
    @PrimaryKey
    private String userID;

    private String count;
    private String userName;
    private String avatar;
    private String role;
    private String gender;

    public UserDB(){

    }

    public UserDB(User user){
        userID = user.getId();
        userName = user.getNickName();
        avatar = user.getAvatar();
        role = user.getRole().getRole();
        gender = user.getGender();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUrseID() {
        return userID;
    }

    public void setUrseID(String userID) {
        this.userID = userID;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
