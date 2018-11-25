package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.base.IBaseView;
import com.example.wanhao.aclassapp.db.ChatDB;
import com.example.wanhao.aclassapp.db.CourseDB;

import java.util.List;


public interface CourseView extends IBaseView<List<ChatDB>>{

    void notifyDataChange(boolean isMove);

    void tokenError();

    void initView(CourseDB course);
}
