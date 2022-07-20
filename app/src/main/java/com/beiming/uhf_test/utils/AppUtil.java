package com.beiming.uhf_test.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import com.beiming.uhf_test.App;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * application相关的工具类
 */

public class AppUtil {

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取app版本号
     * @return
     */
    public static String getAppVersion() {
        String versionName = "";
        try {
            PackageManager pkgMng = App.getInstance().getPackageManager();
            PackageInfo pkgInfo = pkgMng
                    .getPackageInfo(App.getInstance().getPackageName(), 0);
            versionName = pkgInfo.versionName;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 使用 ContextCompat 读取color资源
     * @param id
     * @return
     */
    public static int getCompatColor(int id) {
        return ContextCompat.getColor(App.getInstance(), id);
    }

    /**
     * 使用 ContextCompat 读取drawable资源
     * @param id
     * @return
     */
    public static Drawable getCompatDrawable(int id) {
        return ContextCompat.getDrawable(App.getInstance(), id);
    }

    /**
     * 获取string
     * @param id
     * @return
     */
    public static String getString(int id) {
        return App.getInstance().getString(id);
    }


    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        int result =  (int) (25 * scale + 0.5f);//默认25dp
        int resourceId = App.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = App.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * android5.0以上支持沉浸式状态栏
     * 支持沉浸式的机型，界面顶部需留出状态栏的高度
     * @return
     */
    public static boolean isSupportImmersionBar() {
        return Build.VERSION.SDK_INT >= 21 && (!"OPPO A59s".equals(Build.MODEL));         //21表示5.0
    }

    /**
     * 杀进程
     * 场景：强制升级，用户点击关闭
     */
    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 判断app是否在前台运行
     * @param context
     * @return
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据包名跳转到该app的应用信息界面
     * @return
     */
    public static void goAppDetailSettingActivity(Context context) {
        Uri packageURI = Uri.parse("package:" + getPackageName(context));
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
