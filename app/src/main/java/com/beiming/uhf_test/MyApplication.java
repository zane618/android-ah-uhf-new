package com.beiming.uhf_test;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.beiming.uhf_test.bean.LoginBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;

/**
 * Created by htj on 2020/7/8.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;
    public static Context instance;
    public static LoginBean loginBean;

    public synchronized static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        instance = getApplicationContext();
        GreenDaoManager.setContext(getApplicationContext());
        GreenDaoManager.getInstance();

        /** 初始化 SharedPreferencesUtil  用来存储本地数据*/
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
        SharedPreferencesUtil.setInstance(sharedPreferencesUtil);

    }

    public static synchronized Context getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
