package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

public class ChatResult {
    @SerializedName("message")
    ChatBean message;
    @SerializedName("code")
    String code;

    public ChatBean getMessage() {
        return message;
    }

    public void setMessage(ChatBean message) {
        this.message = message;
    }

    public String getStatus() {
        return code;
    }

    public void setStatus(String status) {
        this.code = status;
    }
}
