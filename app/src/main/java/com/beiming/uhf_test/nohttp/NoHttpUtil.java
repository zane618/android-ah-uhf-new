package com.beiming.uhf_test.nohttp;

import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * nohttp请求工具类
 */

public class NoHttpUtil {
    private static final int REQUEST_COMMON_WHAT = 0; //nohttp框架为了区分是哪个发起的请求，此项目默认不用该值

    /**
     * 发送接口请求
     * @param absRequest
     */
    public static void sendRequest( AbsRequest absRequest) {
        CallServer.getInstance().request(0, absRequest.getRequest(), absRequest);
    }
    /*public static void sendRequest( AbsByteRequest absRequest) {
        CallServer.getInstance().request(0, absRequest.getRequest(), absRequest);
    }

    public static void sendRequest( AbsFileRequest absRequest) {
        CallServer.getInstance().request(0, absRequest.getRequest(), absRequest);
    }

    *//**
     * @param queue
     * @param absRequest
     *//*
    public static void sendRequest(RequestQueue queue, AbsRequest absRequest) {
        queue.add(0, absRequest.getRequest(), absRequest);
    }*/

}
