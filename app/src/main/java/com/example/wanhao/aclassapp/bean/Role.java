package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by wanhao on 2018/4/11.
 */

public class Role implements Serializable{
    @SerializedName("role_id")
    private int roleID;
    @SerializedName("role")
    private String role;

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int role) {
        this.roleID = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
