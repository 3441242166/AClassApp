package com.example.wanhao.aclassapp.bean;

/**
 * Created by wanhao on 2017/5/13.
 */

public class HttpResult<T> {

    private String code;

    private String message;

    private T data;

    public String getCode() {
        return code;
    }

    public HttpResult<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HttpResult<T> setMessage(String msg) {
        this.message = msg;
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
