package com.example.wanhao.aclassapp.base;

/**
 * Created by wanhao on 2018/1/21.
 */

public class BasePresenterImp<V extends IBaseView, T> implements IBaseRequestCallBack<T> {

    private IBaseView iBaseView = null;  //基类视图

    /**
     * @descriptoin  构造方法
     * @author  dc
     * @param view 具体业务的视图接口对象
     * @date 2017/2/16 15:12
     */
    public BasePresenterImp(V view) {
        this.iBaseView = view;
    }

    /**
     * @descriptoin 请求之前显示progress
     * @author  dc
     * @date 2017/2/16 15:13
     */
    @Override
    public void beforeRequest() {
        iBaseView.showProgress();
    }

    /**
     * @descriptoin 请求异常显示异常信息
     * @author  dc
     * @param throwable 异常信息
     * @date 2017/2/16 15:13
     */
    @Override
    public void requestError(Throwable throwable) {
        iBaseView.loadDataError(throwable.toString());
        iBaseView.dismissProgress(); //请求错误，提示错误信息之后隐藏progress
    }

    /**
     * @descriptoin 请求完成之后隐藏progress
     * @author  dc
     * @date 2017/2/16 15:14
     */
    @Override
    public void requestComplete() {
        iBaseView.dismissProgress();
    }

    /**
     * @descriptoin 请求成功获取成功之后的数据信息
     * @author  dc
     * @param callBack 回调的数据
     * @date 2017/2/16 15:14
     */
    @Override
    public void requestSuccess(T callBack) {
        iBaseView.loadDataSuccess(callBack);
    }

}
