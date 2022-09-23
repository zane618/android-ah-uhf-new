package com.beiming.uhf_test.utils;

import android.os.Environment;

import com.beiming.uhf_test.App;

import java.io.File;


/**
 * Created by htj on 2020/7/10.
 */

public class ConstantUtil {

    public static final String NORMAL_DIR = Environment.getExternalStorageDirectory() + File.separator + "/UHF";
    public static final String IMAGE_STR = NORMAL_DIR + "/image/";
    public static final String EXCEL_STR = NORMAL_DIR + "/excel/";
    public static final String FILE_PROVIDER = "com.beiming.uhf_test";//6.0后读写权限的内容提供者

    public static final String LAST_LOCATION = "lastLocation";//获取当前最后一次定位信息
    public static final String REFRESH_PIC_RES_LIST_FROM_CAMERA = "REFRESH_PIC_RES_LIST_FROM_CAMERA";//刷新图片名称集合从拍照页面返回上一界面
    public static final String PHOTO_BEAN_LIST = "PhotoBeans";//组件之间传递photoBean集合的key
    public static final String REFRESH_PIC_DES_LIST_FROM_PREVIEW = "REFRESH_PIC_DES_LIST_FROM_PREVIEW";//刷新图片描述集合(从图片预览界面返回上级界面)
    public static final String CLEAR_READ_TAG_DATA = "CLEAR_READ_TAG_DATA";//成功保存后，清除扫描标签中的数据
    public static final String CREAT_OR_DETAILS = "CREAT_OR_DETAILS";//判断是新建还是详情界面(0:新建,1:详情)
    public static final int NULL_PERMISSIONS_REQUEST = 10001;


}
