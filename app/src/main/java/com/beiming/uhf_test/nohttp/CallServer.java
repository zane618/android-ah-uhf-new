package com.beiming.uhf_test.nohttp;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 */

public class CallServer {
    private static final int THREAD_POOL_SIZE = 8;
    private static CallServer instance;
    private RequestQueue queue;

    public static CallServer getInstance() {
        if (instance == null)
            synchronized (CallServer.class) {
                if (instance == null)
                    instance = new CallServer();
            }
        return instance;
    }

    private CallServer() {
        queue = NoHttp.newRequestQueue(THREAD_POOL_SIZE);
    }

    public <T> void request(int what, Request<T> request, OnResponseListener<T> listener) {
        queue.add(what, request, listener);
    }

    /**
     * 取消当前的所有请求
     */
    public void cancelAll() {
        queue.cancelAll();
    }

    // 完全退出app时，调用这个方法释放CPU。
    public void stop() {
        queue.stop();
    }
}
