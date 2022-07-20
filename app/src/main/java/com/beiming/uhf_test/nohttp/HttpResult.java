package com.beiming.uhf_test.nohttp;

/**
 * 接口返回结构体
 * Created by duantianhui on 2017/8/2.
 */

public class HttpResult<T> {
    private String reCode;      //服务器返回的code
    private String reInfo;      //服务器返回的信息
    private T result;             //数据

    public String getReCode() {
        return reCode;
    }

    public void setReCode(String reCode) {
        this.reCode = reCode;
    }

    public String getReInfo() {
        return reInfo;
    }

    public HttpResult setReInfo(String reInfo) {
        this.reInfo = reInfo;
        return this;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
