package com.beiming.uhf_test.nohttp;

/**
 * 接口请求相关常量
 * Created by duantianhui on 2018/6/14.
 */

public interface NetConsts {
    int HTTP_REQUEST_TIME = 30 * 1000;                  //get、post  http访问标准超时时间 10s
    int HTTP_REQUEST_LONG_TIME = 18 * 1000;             //加长的超时时间

    String SUCCESS = "0";             //接口业务逻辑成功
    String FAIL_NOTICE = "1";             //接口业务逻辑失败 ，只提示用户
    String FAIL_DIALOG_NOTICE = "2";             //接口业务逻辑失败 需要用户参与交互，点击确定按钮
    String FAIL_REFRESH_TOKEN = "3";             //刷新TOKEN
    String FAIL_NEED_EXIT = "4";             //重新登陆来获取TOKEN
    String FAIL_UPGRADE_APP = "5";             //需要升级客户端

}
