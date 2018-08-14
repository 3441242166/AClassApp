package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhao on 2018/3/7.
 */

public class Document {
    @SerializedName("id")
    private int id;
    @SerializedName("position")
    private String title;
    @SerializedName("size")
    private String size;
    @SerializedName("author")
    private String user;
    @SerializedName("date")
    private String date;

    private String courseID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
}
