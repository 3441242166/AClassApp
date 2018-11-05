package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.bean.ChatBean;

import java.util.List;

public interface CourseView {
    void errorMessage(String message);

    void startActivity(Class activity,String data);

    void getMessage(ChatBean message);

    void getHistoryMessage(List<ChatBean> list);

    void tokenError();
}
