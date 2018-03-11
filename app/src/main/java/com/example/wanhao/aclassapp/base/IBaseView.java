package com.example.wanhao.aclassapp.base;

/**
 * Created by wanhao on 2018/1/21.
 */

public interface IBaseView<T> {
    /**
     * @descriptoin  请求前加载progress
     * @author   dc
     * @date 2017/2/16 11:00
     */
    void showProgress();

    /**
     * @descriptoin  请求结束之后隐藏progress
     * @author   dc
     * @date 2017/2/16 11:01
     */
    void disimissProgress();

    /**
     * @descriptoin  请求数据成功
     * @author   dc
     * @param tData 数据类型
     * @date 2017/2/16 11:01
     */
    void loadDataSuccess(T tData);

    /**
     * @descriptoin  请求数据错误
     * @author   dc
     * @param throwable 异常类型
     * @date 2017/2/16 11:01
     */
    void loadDataError(String throwable);

    void tokenError(String msg);

}

