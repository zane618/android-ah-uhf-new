package com.beiming.uhf_test.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.bean.FileBean;
import com.beiming.uhf_test.utils.pic.OtherUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by htj on 2019/4/17.
 */

public class FileUtils {
    private static FileUtils instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;
    public static final String POINT = ".";


    public static FileUtils getInstance(Context context) {
        if (instance == null) instance = new FileUtils(context);
        return instance;
    }

    private FileUtils(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public FileUtils copyAssetsToSD(final boolean isZip, final String srcPath, final String sdPath, final String sdSrc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //?????????????????????????????????????????????
//                copyAssetsToDst(context, srcPath, sdPath);
                //??????????????????y???????????????????????????
                if (isZip) {
                    try {
                        UnZipAssetsFolder(context, srcPath, sdPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //?????????????????????????????????????????????
                    copyAssetsToDst(context, srcPath, sdPath, sdSrc);
                }

                if (isSuccess) handler.obtainMessage(SUCCESS).sendToTarget();
                else handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath, String sdSrc) {
        File packge = new File(sdSrc);
        if (!packge.exists()) {
            packge.mkdirs();
        }
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(sdSrc, dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets ?????????????????????
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName, sdSrc);
                    } else { // assets ?????????
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName, sdSrc);
                    }
                }
            } else {
                File outFile = new File(sdSrc, dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            isSuccess = false;
        }
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

    public boolean fileIsExsits(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }


    /*private void install(String filePath) {
        try {
            InputStream open = context.getAssets().open("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "??????????????????: " + filePath);
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w(TAG, "???????????? N ??????????????? fileProvider ????????????");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    mContext
                    , "????????????.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w(TAG, "??????????????????");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }*/

    public void install(Context context) {
        String str = "/amap/tencent.apk";
        String fileName = Environment.getExternalStorageDirectory() + str;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * ??????assets????????????zip??????????????????
     *
     * @param zipFileString ZIP?????????????????????????????????xxx.zip
     * @param outPathString ??????????????????
     * @throws Exception
     */
    public static void UnZipAssetsFolder(Context context, String zipFileString, String
            outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(context.getAssets().open(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //???????????????????????????
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(Environment.getExternalStorageDirectory(), outPathString + File.separator + szName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // ????????????????????????
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // ????????????????????????????????????
                while ((len = inZip.read(buffer)) != -1) {
                    // ???????????????0?????????????????????????????????
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param filePath ?????????
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
     * * ??????Android4.4????????????Uri??????????????????????????????????????????????????????
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
     * ??????????????????
     *
     * @param path ??????????????????
     * @return
     */
    public static String getFileSize(String path) {
        File file = new File(path);
        return FormetFileSize(file.length());
    }

    /**
     * ??????????????????
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
     * ???????????????????????????
     * <p>
     * path ????????????
     * i 0;???????????????  1:???????????????
     *
     * @return
     */
    public static String getFileSuffix(String path, int i) {
        if (!TextUtils.isEmpty(path)) {
            int start = 0;
            switch (i) {
                case 0:
                    start = path.lastIndexOf(".");//???????????????????????????????????????????????????????????????????????????????????????
                    break;
                case 1:
                    start = path.lastIndexOf("/");//???????????????????????????????????????????????????????????????????????????????????????
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

    //??????????????????????????????
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


    /**
     * ??????????????????
     */
    public static boolean isAppInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /***
     * ???????????????????????????uri
     * @param context
     * @param path ???????????????(?????????)?????????
     * @return
     */
    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[]{path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if (cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }

    /**
     * ??????excel???????????????excel??????
     */
    public static List<FileBean> getExcelFileList() {
        String dirPath;
        dirPath = ConstantUtil.EXCEL_STR;
        List<FileBean> list = new ArrayList<>();
        //???????????????????????????excle????????????
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        for (File childrenFile : files) {
            String absolutePath = childrenFile.getAbsolutePath();
            if (isExcle(absolutePath)) {
                FileBean fileBean = new FileBean();
                fileBean.setFileId(null);
                fileBean.setRes(absolutePath);
                fileBean.setFileName(childrenFile.getName());
                fileBean.setUploadFileType("");
                fileBean.setFileType("3");
                list.add(fileBean);
            }
        }
        return list;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @return
     */
    public static boolean isExistFile(String fileSrc, List<FileBean> list) {
        for (FileBean fileBean : list) {
            if (fileSrc.equals(fileBean.getRes())) {
                return true;
            }
        }
        return false;
    }

    /**
     * ???????????????excle??????
     */
    public static boolean isExcle(String absolutePath) {
        String str = "";
        if (absolutePath.contains(POINT)) {
            str = absolutePath.substring(absolutePath.lastIndexOf(POINT) + 1, absolutePath.length());
        }
        if (str.equals("xls") || str.equals("XLS") /*|| str.equals("xlsx") || str.equals("XLSX")*/) {
            return true;
        } else {
            return false;
        }
    }

    //??????????????????
    public static void initFile() {
        String dirPath1 = ConstantUtil.EXCEL_STR;
        //???????????????????????????excle????????????
        File file1 = new File(dirPath1);
        if (!file1.exists()) {
            file1.mkdirs();
            FileUtils.notifySystemToScan(dirPath1);
        }
        String dirPath4 = ConstantUtil.IMAGE_STR;
        //???????????????????????????excle????????????
        File file4 = new File(dirPath4);
        if (!file4.exists()) {
            file4.mkdirs();
            FileUtils.notifySystemToScan(dirPath4);
        }
    }

    //????????????????????????
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 8      * ??????txt???????????????
     * 9      * @param file ???????????????????????????
     * 10      * @return ??????????????????
     * 11
     */
    public static String txt2String(Context context, String fileName) {
        try {
            InputStream read = context.getResources().getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(read);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String result = "";
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                result = result + lineTxt;
            }
            bufferedReader.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
