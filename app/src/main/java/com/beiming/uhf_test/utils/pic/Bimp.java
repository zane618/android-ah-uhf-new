package com.beiming.uhf_test.utils.pic;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beiming.uhf_test.utils.pic.model.ImageItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Description：出来临时图片数据
 * <p>
 * Created by Mjj on 2016/12/2.
 */

public class Bimp {

    public static int max = 0;

    public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   // 图片临时列表

//    public static Bitmap revitionImageSize(String path) throws IOException {
//        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
//                new File(path)));
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(in, null, options);
//        in.close();
//        int i = 0;
//        Bitmap bitmap = null;
//        while (true) {
//            if ((options.outWidth >> i <= 1000)
//                    && (options.outHeight >> i <= 1000)) {
//                in = new BufferedInputStream(
//                        new FileInputStream(new File(path)));
//                options.inSampleSize = (int) Math.pow(2.0D, i);
//                options.inJustDecodeBounds = false;
//                bitmap = BitmapFactory.decodeStream(in, null, options);
//                break;
//            }
//            i += 1;
//        }
//        return bitmap;
//    }

    public static List<String> mDrr = new ArrayList<String>();
    public static List<Bitmap> mBmps = new ArrayList<Bitmap>();

    public static void refreshBitmapList() {
        getBitmapList();
        getBitmaps();
    }

    public static List<String> getBitmapList() {
        mDrr.clear();
        for (int i = 0; i < tempSelectBitmap.size(); i++) {
            mDrr.add(tempSelectBitmap.get(i).getImageId());
        }
        return mDrr;
    }

    public static List<Bitmap> getBitmaps() {
        mBmps.clear();
        for (int i = 0; i < tempSelectBitmap.size(); i++) {
            mBmps.add(tempSelectBitmap.get(i).getBitmap());
        }
        return mBmps;
    }


//    图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于120KB，失真度不明显
//    public static List<String> mDrr = new ArrayList<String>();
//    public static List<Bitmap> mBmps = new ArrayList<Bitmap>();
//    public static boolean mActBool = true;
//    public static int mMax = 0;

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1400) && (options.outHeight >> i <= 1400)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

}
