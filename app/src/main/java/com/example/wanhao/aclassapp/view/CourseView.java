package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.db.ChatDB;
import com.example.wanhao.aclassapp.db.CourseDB;

import java.util.List;

public interface CourseView {
    void errorMessage(String message);

    void startActivity(Class activity,String data);

    void getMessage(ChatDB message);

    void getHistoryMessage(List<ChatDB> list);

    void tokenError();

    void initView(CourseDB course);
}
