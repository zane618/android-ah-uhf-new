package com.beiming.uhf_test;


import android.content.Context;

import com.beiming.uhf_test.bean.LoginBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.kongzue.baseframework.BaseApp;

/**
 * Created by htj on 2020/7/8.
 */

public class MyApplication extends BaseApp<MyApplication> {
    private static MyApplication instance;
    public static Context mContext;
    public static LoginBean loginBean;

    public synchronized static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void init() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        GreenDaoManager.setContext(getApplicationContext());
        GreenDaoManager.getInstance();

        /** 初始化 SharedPreferencesUtil  用来存储本地数据*/
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
        SharedPreferencesUtil.setInstance(sharedPreferencesUtil);

        //初始化 uni小程序SDK ----start----------
        /*MenuActionSheetItem item = new MenuActionSheetItem("关于", "gy");
        MenuActionSheetItem item1 = new MenuActionSheetItem("获取当前页面url", "hqdqym");
        MenuActionSheetItem item2 = new MenuActionSheetItem("跳转到宿主原生测试页面", "gotoTestPage");
        List<MenuActionSheetItem> sheetItems = new ArrayList<>();
        sheetItems.add(item);
        sheetItems.add(item1);
        sheetItems.add(item2);
        Log.i("unimp","onCreate----");
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(true)
                .setMenuDefFontSize("16px")
                .setMenuDefFontColor("#ff00ff")
                .setMenuDefFontWeight("normal")
                .setMenuActionSheetItems(sheetItems)
                .setEnableBackground(false)//开启后台运行
                .build();
        DCUniMPSDK.getInstance().initialize(this, config, new IDCUniMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean b) {
                Log.i("unimp","onInitFinished----"+b);
            }
        });*/
        //初始化 uni小程序SDK ----end----------

    }

    public static synchronized Context getContext() {
        return mContext;
    }
}
