package com.beiming.uhf_test.utils;

import android.util.Log;

import com.beiming.uhf_test.BuildConfig;


/**
 * 日志操作类,打印日志所用
 * Created by duantianhui on 2018/6/14.
 */
public class LogPrintUtil {

    // 日志开关 true为开启 false关闭
    private static final boolean isPrintLog = BuildConfig.LOG_DEBUG;

    public static final String TAG = "wtyt_lgklb";

    public static void log(String msg) {
        if(isPrintLog) {
            Log.i(TAG, msg);
        }
    }

    public static void userInfo(String msg) {
        if (isPrintLog) {
            Log.e("userInfo", msg);
        }
    }

    public static void dbLog(String msg) {
        if (isPrintLog) {
            Log.i("dbLog", msg);
        }
    }

    public static void httpLog(String msg) {
        if (isPrintLog) {
            Log.i("httpLog", msg);
        }
    }

    public static void zhangshi(String msg) {
        if (isPrintLog) {
            Log.e("zhangshi", msg);
        }
    }


    public static void baseActivity(String msg) {
        if (isPrintLog) {
            Log.e("baseActivity", msg);
        }
    }
    public static void baseFragment(String msg) {
        if (isPrintLog) {
            Log.e("baseFragment", msg);
        }
    }
}
