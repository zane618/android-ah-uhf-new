package com.beiming.uhf_test.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by htj on 2018/11/14.
 */

public class PermissionUtils {
    public static String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    public static void initPermission(Context context, int requestNumber) {
        if (!isPermissionsGranted(context)) {
            getPermissions(context, requestNumber);
        }
    }

    public static boolean isPermissionsGranted(Context context) {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(context, permissions)) {
            isPermission = true;
        } else {
            isPermission = false;
        }
        return isPermission;
    }

    public static void getPermissions(Context context, int requestNumber) {
        EasyPermissions.requestPermissions((Activity) context, "请允许必要的权限", requestNumber, permissions);
    }

}
