package com.beiming.uhf_test;


import android.content.Context;
import android.util.Log;

import com.beiming.uhf_test.bean.LoginBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.nohttp.NetConsts;
import com.beiming.uhf_test.utils.AppUtil;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.kongzue.baseframework.BaseApp;
import com.lxj.xpopup.XPopup;
import com.tencent.mmkv.MMKV;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.MenuActionSheetItem;
import okhttp3.OkHttpClient;

/**
 * Created by htj on 2020/7/8.
 */

public class App extends BaseApp<App> {
    private static App instance;
    public static Context mContext;
    public static LoginBean loginBean;
    private String appVersion = "";

    public synchronized static App getInstance() {
        return instance;
    }

    @Override
    public void init() {
        initNoHttp();
        initOkhttpUtils();
        appVersion = AppUtil.getVersionName(this);
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

        //
        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
        XPopup.setAnimationDuration(200);
        //MMKV初始化
        String rootDir = MMKV.initialize(this);
        LogPrintUtil.zhangshi("mmkv:rootDir:" + rootDir);

        //初始化 uni小程序SDK ----start----------
        MenuActionSheetItem item = new MenuActionSheetItem("关于", "gy");
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
        });
        //初始化 uni小程序SDK ----end----------

    }

    private void initOkhttpUtils() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
//                .cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 初始化noHttp框架
     */
    private void initNoHttp() {
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .connectionTimeout(NetConsts.HTTP_REQUEST_TIME)
                .readTimeout(NetConsts.HTTP_REQUEST_TIME)
                .cacheStore(new DBCacheStore(this).setEnable(false))
                .cookieStore(new DBCookieStore(this).setEnable(false))
                .networkExecutor(new OkHttpNetworkExecutor())
                .build()
        );
    }

    public String getAppVersion() {
        return appVersion;
    }

    public static synchronized Context getContext() {
        return mContext;
    }
}
