package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhao on 2018/4/11.
 */

public class Role {
    @SerializedName("role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
