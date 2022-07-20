package com.beiming.uhf_test.helper;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.beiming.uhf_test.utils.LogPrintUtil;

/**
 * 三方SDK初
 */
public class AppThirdSdkHelper {

    public static void initSdk(Application application) {
        LogPrintUtil.zhangshi("初始化第三方SDK");
        if (isMainProcess(application)) {
            //高德合规
            initGaodePrivacy(application);
        }
    }


    /**
     * 高德合规接口
     */
    private static void initGaodePrivacy(Application application) {
//        AMapLocationClient.updatePrivacyShow(application, true, true);
//        AMapLocationClient.updatePrivacyAgree(application, true);
    }

    /**
     * 判断是否是主进程
     *
     * @return
     */
    private static boolean isMainProcess(Application application) {
        return (application.getApplicationInfo().packageName.equals(getCurProcessName(application.getApplicationContext())));
    }

    private static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
