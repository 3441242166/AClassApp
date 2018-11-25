package com.example.wanhao.aclassapp.base;

import com.example.wanhao.aclassapp.db.CourseDB;

import java.util.List;

/**
 * Created by wanhao on 2018/1/21.
 */

public interface IBaseView<T> {

    void showProgress();

    void dismissProgress();

    void loadDataSuccess(T data);

    void errorMessage(String throwable);

}

