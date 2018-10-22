package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wanhao on 2018/3/7.
 */

public class Document implements Serializable{
    @SerializedName("file_id")
    private int id;
    @SerializedName("file_name")
    private String title;
    @SerializedName("size")
    private String size;
    @SerializedName("file_author")
    private String user;
    @SerializedName("file_date")
    private String date;
    @SerializedName("fileFormat")
    private String type;
    @SerializedName("position")
    private String path;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
