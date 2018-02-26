package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhao on 2018/2/24.
 */

public class Course {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    private String name;
    @SerializedName("major")
    private String parent;
    @SerializedName("count")
    private String num;
    @SerializedName("picture")
    private String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
