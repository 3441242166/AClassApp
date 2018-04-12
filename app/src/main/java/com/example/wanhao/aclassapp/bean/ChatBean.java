package com.example.wanhao.aclassapp.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by wanhao on 2018/4/5.
 */

public class ChatBean implements MultiItemEntity {
    public static final int ME = 1;
    public static final int OTHER = 2;

    int headImage;
    String name;
    String content;

    int type;

    public int getHeadImage() {
        return headImage;
    }

    public void setHeadImage(int headImage) {
        this.headImage = headImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
