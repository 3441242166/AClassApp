package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

public class ChatResult {
    @SerializedName("message")
    ChatBean message;
    @SerializedName("status")
    String status;

    public ChatBean getMessage() {
        return message;
    }

    public void setMessage(ChatBean message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
