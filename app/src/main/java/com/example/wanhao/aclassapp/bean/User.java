package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class User implements Serializable{
    @SerializedName("user_id")
    private String id;
    @SerializedName("user_name")
    private String count;
    @SerializedName("nickname")
    private String nickName;
    @SerializedName("gender")
    private String gender;
    @SerializedName("signature")
    private String signature;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("role")
    private Role role;
    @SerializedName("password")
    private String token;

    private int roleID;

    public User(){
        role = new Role();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
