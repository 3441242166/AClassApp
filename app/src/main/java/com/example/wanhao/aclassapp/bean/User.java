package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("nickname")
    private String nickName;
    @SerializedName("gender")
    private String gender;
    @SerializedName("signature")
    private String signature;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("role")
    Role role;

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
}
