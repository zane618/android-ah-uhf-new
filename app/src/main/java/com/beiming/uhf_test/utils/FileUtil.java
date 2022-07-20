package com.beiming.uhf_test.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.utils.pic.OtherUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by htj on 2018/11/6.
 */

public class FileUtil {

    /**
     * 通知系统对文件进行扫面并刷新
     *
     * @param filePath 文件名
     */
    public static void notifySystemToScan(String filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        App.getInstance().sendBroadcast(intent);
    }


    public static String getRealPathFromURI(final Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

//                return getDataColumn(context, contentUri, null, null);
                return "";
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 140      * Get the value of the data column for this Uri. This is useful for
     * 141      * MediaStore Uris, and other file-based ContentProviders.
     * 142      *
     * 143      * @param context       The context.
     * 144      * @param uri           The Uri to query.
     * 145      * @param selection     (Optional) Filter used in the query.
     * 146      * @param selectionArgs (Optional) Selection arguments used in the query.
     * 147      * @return The value of the _data column, which is typically a file path.
     * 148
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 171      * @param uri The Uri to check.
     * 172      * @return Whether the Uri authority is ExternalStorageProvider.
     * 173
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 179      * @param uri The Uri to check.
     * 180      * @return Whether the Uri authority is DownloadsProvider.
     * 181
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 187      * @param uri The Uri to check.
     * 188      * @return Whether the Uri authority is MediaProvider.
     * 189
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取文件大小
     *
     * @param path 文件绝对路径
     * @return
     */
    public static String getFileSize(String path) {
        File file = new File(path);
        return FormetFileSize(file.length());
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 根据路劲获取后缀名
     * <p>
     * path 文件路劲
     * i 0;获取后缀名  1:获取文件名
     *
     * @return
     */
    public static String getFileSuffix(String path, int i) {
        if (!TextUtils.isEmpty(path)) {
            int start = 0;
            switch (i) {
                case 0:
                    start = path.lastIndexOf(".");//这样获取的就是名称，如果双引号里面是点的话获取的就是后缀名
                    break;
                case 1:
                    start = path.lastIndexOf("/");//这样获取的就是名称，如果双引号里面是点的话获取的就是后缀名
                    break;
            }
            String format = "";
            if (start < 0) {
                format = path;
            } else {
                format = path.substring(start + 1);
            }
            return format;
        }
        return "";
    }

    //判断文件大小是否相同
    private boolean isEquasForFileSize(String picPath, List<String> attachmentPicPaths) {
        boolean isSame = false;
        File newAddPhoto = new File(picPath);
        File oldExsistPhoto;
        for (String str : attachmentPicPaths) {
            oldExsistPhoto = new File(str);
            if (OtherUtils.getFileMD5(newAddPhoto).equals(OtherUtils.getFileMD5(oldExsistPhoto))
                    && OtherUtils.getFileSha1(newAddPhoto).equals(OtherUtils.getFileSha1(oldExsistPhoto))) {
                isSame = true;
            }
        }
        return isSame;
    }
}
