package com.beiming.uhf_test.utils.pic;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.ConstantUtil;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * Description：屏幕、尺寸转换工具类
 * <p>
 * Created by Mjj on 2016/12/2.
 */

public class OtherUtils {

    /**
     * 判断外部存储卡是否可用
     *
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public static final int getHeightInPx(Context context) {
        final int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final int getWidthInPx(Context context) {
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        int widthInDp = px2dip(context, width);
        return widthInDp;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断图片在原有的集合中是否存在
     *
     * @return
     */
    public static boolean isExistPhoto(String imgStr, List<PhotoBean> list) {
        for (int i = 0; i < list.size(); i++) {
            if (imgStr.equals(list.get(i).getDescribe())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据string.xml资源格式化字符串
     *
     * @param context
     * @param resource
     * @param args
     * @return
     */
    public static String formatResourceString(Context context, int resource, Object... args) {
        String str = context.getResources().getString(resource);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return String.format(str, args);
    }

    public static String imgStr;

    /**
     * 获取拍照相片存储文件
     *
     * @param context
     * @return
     */
    public static File createFile(Context context) {
        String strPath = ConstantUtil.IMAGE_STR;
        File packge = new File(strPath);
        if (!packge.exists()) {
            packge.mkdirs();
        }
        File[] fileList = packge.listFiles();
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String imgName = PhotoUtils.getImageName();
            file = new File(strPath + imgName);
            imgStr = strPath + imgName;
        } /*else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, "nari/" + mark + "/image/" + imageName + "/" + timeStamp + ".JPG");
        }*/
        return file;
    }

    public static ArrayList<String> getPhotoList(Context context, String imagePackgeName) {
        ArrayList<String> strPhotoList = new ArrayList<String>();
        File packge = new File(imagePackgeName);
        if (!packge.exists()) {
            packge.mkdirs();
        }
        File[] fileList = packge.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (File file : fileList) {
                strPhotoList.add(file.getAbsolutePath());
            }
        }
        return strPhotoList;
    }

    public static void deletePackge(Context context, String imagePackgeName) {
        File packge = new File(imagePackgeName);
        if (packge.exists() && packge.isDirectory()) {
            File[] fileList = packge.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file : fileList) {
                    file.delete();
                }
            }
            packge.delete();
        }

    }
/*    public static LinkedList<File> listLinkedFiles(String strPath) {
        LinkedList<File> list = new LinkedList<File>();
        File dir = new File(strPath);
        File[] file = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file.isDirectory())
                list.add(file[i]);
            else
                System.out.println(file.getAbsolutePath());
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file.isDirectory())
                        list.add(file);
                    else
                        System.out.println(file.getAbsolutePath());
                }
            } else {
                System.out.println(tmp.getAbsolutePath());
            }
        }
        return list;
    }*/

    /**
     * 获取磁盘缓存文件
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用程序版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String getRecordStr(String mark, String packgeName) {
        String recordPackgeName = Environment.getExternalStorageDirectory() +
                File.separator + "作物收集系统/本地录音、图片及word模板文件/" + mark + "/audio_recorder/" + packgeName + "/";
        return recordPackgeName;
    }

    public static String getPhotoStr() {
        String photoPackgeName = Environment.getExternalStorageDirectory() +
                File.separator + "作物收集系统/本地录音、图片及word模板文件/" + "/image/" + "/";
        return photoPackgeName;
    }

    public static String getUpLoadPhotoStr() {
        String photoPackgeName = Environment.getExternalStorageDirectory() +
                File.separator + "作物收集系统/数据库及照片（电子上交版）/";
        return photoPackgeName;
    }

    public static String getCollectPhotoStr() {
        String photoPackgeName = Environment.getExternalStorageDirectory() +
                File.separator + "作物收集系统/征集表/图片文件/";
        return photoPackgeName;
    }

    // 计算文件的 MD5 值
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // 计算文件的 SHA-1 值
    public static String getFileSha1(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断永久编号的是否符合正则
     *
     * @param yjbh 永久编号
     * @return
     */
    public static boolean isLegal(String yjbh) {
        String regex = "^[a-z0-9A-Z]+$";
        if (yjbh.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

}
