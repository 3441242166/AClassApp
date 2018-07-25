package com.example.wanhao.aclassapp.bean.requestbean;

/**
 * Created by wanhao on 2017/5/13.
 */

public class HttpResult<T> {

    String code;

    String msg;

    private T data;

    public String getCode() {
        return code;
    }

    public HttpResult<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public HttpResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public HttpResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
