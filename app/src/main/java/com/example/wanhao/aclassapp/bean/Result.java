package com.example.wanhao.aclassapp.bean;

import java.util.List;

/**
 * Created by wanhao on 2018/3/20.
 */

public class Result<T> {

    private String status;

    private List<T> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
