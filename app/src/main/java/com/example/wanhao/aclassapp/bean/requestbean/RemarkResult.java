package com.example.wanhao.aclassapp.bean.requestbean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wanhao on 2018/4/11.
 */

public class RemarkResult {
    @SerializedName("status")
    String status;
    @SerializedName("comments")
    List<Remark> list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Remark> getList() {
        return list;
    }

    public void setList(List<Remark> list) {
        this.list = list;
    }
}
