package com.example.wanhao.aclassapp.base;

/**
 * Created by wanhao on 2018/1/21.
 */

public interface IBaseView<T> {
    /**
     * @descriptoin  请求前加载progress
     * @date 2017/2/16 11:00
     */
    void showProgress();

    /**
     * @descriptoin  请求结束之后隐藏progress
     * @date 2017/2/16 11:01
     */
    void dismissProgress();

    /**
     * @descriptoin  请求数据成功
     * @param data 数据类型
     * @date 2017/2/16 11:01
     */
    void loadDataSuccess(T data);

    /**
     * @descriptoin  请求数据错误
     * @param throwable 异常类型
     * @date 2017/2/16 11:01
     */
    void loadDataError(String throwable);

}

