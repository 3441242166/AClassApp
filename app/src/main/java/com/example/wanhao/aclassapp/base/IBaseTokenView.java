package com.example.wanhao.aclassapp.base;

/**
 * Created by wanhao on 2018/3/27.
 */

public interface IBaseTokenView<T> extends IBaseView<T> {

    void tokenError(String msg);

}
