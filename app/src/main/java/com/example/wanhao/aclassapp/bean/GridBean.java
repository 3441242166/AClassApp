package com.example.wanhao.aclassapp.bean;

/**
 * Created by wanhao on 2018/3/10.
 */

public class GridBean {
    private int imgID;
    private String name;

    public GridBean(int imgID,String name){
        this.imgID = imgID;
        this.name = name;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
