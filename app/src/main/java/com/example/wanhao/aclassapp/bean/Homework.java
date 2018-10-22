package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wanhao on 2018/4/8.
 */

public class Homework implements Serializable{
    @SerializedName("course_title_id")
    int id;
    @SerializedName("course_title_name")
    String title;
    @SerializedName("course_title_date")
    String date;
    @SerializedName("homework_sum")
    int sum;

    boolean isDo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDo() {
        return isDo;
    }

    public void setDo(boolean aDo) {
        isDo = aDo;
    }
}
