package com.example.wanhao.aclassapp.bean;

/**
 * Created by wanhao on 2018/4/12.
 */

public class RemarkRequset {
    String content;
    int reply;

    public RemarkRequset(int reply,String content){
        this.reply = reply;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }
}
