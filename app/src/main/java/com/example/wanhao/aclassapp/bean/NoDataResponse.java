package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhao on 2018/2/25.
 */

public class NoDataResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
