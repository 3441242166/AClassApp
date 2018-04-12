package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wanhao on 2018/3/20.
 */

public class DocumentResult {

    private String status;
    @SerializedName("data")
    private List<Document> courses;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Document> getCourses() {
        return courses;
    }

    public void setCourses(List<Document> courses) {
        this.courses = courses;
    }

}
