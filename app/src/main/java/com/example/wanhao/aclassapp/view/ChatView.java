package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.bean.sqlbean.ChatBean;

import java.util.List;

/**
 * Created by wanhao on 2018/4/8.
 */

public interface ChatView {

    void error(String message);

    void newNewMessage(ChatBean message);

    void getHistoryMessage(List<ChatBean> list);

    void tokenError();
}
