package com.beiming.uhf_test.utils.pic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


public class BitmapUtils {

    private static final int BLACK = 0xff000000;

    public static Bitmap getBitmapFormUri(Context context, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if (originalWidth > originalHeight && originalWidth > ww) {
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return bitmap;
    }

    public static void displayImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    public static void displayImage(Context context, String url, int errorId, ImageView view) {
        if (ObjectUtils.isNull(url)) {
            view.setImageResource(errorId);
            return;
        }
        Glide.with(context).load(url).error(errorId).diskCacheStrategy(DiskCacheStrategy.NONE).into(view);
    }

    public static void displayImage(Context context, String imgName, String imgPackgeName, int errorId, ImageView view) {
        if (ObjectUtils.isNull(imgName)) {
            view.setImageResource(errorId);
            return;
        }
        Glide.with(context).load(ConstantUtil.IMAGE_STR + imgPackgeName + "/" + imgName).error(errorId).diskCacheStrategy(DiskCacheStrategy.NONE).into(view);
    }

    public static void displayImageNoCache(Context context, String url, int errorId, ImageView view) {
        if (ObjectUtils.isNull(url)) {
            view.setImageResource(errorId);
            return;
        }
        Glide.with(context).load(url).error(errorId).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(view);
//        Glide.with(context).load(new File(url)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(view);
    }


    /**
     * ????????????
     */
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // ???????????????????????????
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // ?????????????????????????????????????????????
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // ???????????????????????????????????????inSampleSize???????????????????????????????????????????????????
            // ?????????????????????????????????????????????
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String res, int reqWidth, int reqHeight) {
        // ??????????????????inJustDecodeBounds?????????true????????????????????????
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res, options);
        // ?????????????????????????????????inSampleSize???
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // ??????????????????inSampleSize?????????????????????
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(res, options);
    }

    /**
     * ????????????????????????Logo??????
     */
   /* private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        // ?????????????????????
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo?????????????????????????????????1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }
*/
    //??????????????????base64
    @SuppressLint("NewApi")
    public static String getBitmapBase64(String filePath) {
/*        String cacheStr = ConstantUtil.DATA_DATA_DIR_PATH + "/image/";
        String fileName = TimeUtils.currentTime() + ".jpg";
        File fileMd = new File(cacheStr);//???????????????????????????
        if (!fileMd.exists()) {
            fileMd.mkdirs();
        }
        File file = new File(cacheStr + fileName);//???????????????????????????*/
        Bitmap bm = getSmallBitmap(filePath);
        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //???bitmap?????????????????? Bitmap.CompressFormat.PNG ???????????????100???????????????baos????????????
//            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //1.5M???????????????100Kb?????????????????????,??????????????????=94486??????,??????????????????=74473??????
            //?????????JPEG ????????????PNG????????????????????????600kB??????
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] datas = bos.toByteArray();
//            bos.flush();
//            bos.close();
            //??????????????????????????????????????????
//            return Base64Util.encodeBase64File(cacheStr);

            return Base64.getEncoder().encodeToString(datas);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * ???batmap ???file
     *
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//???????????????????????????
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
/*

    */
/**
 * ???????????????base64??????
 *
 * @param imgurl
 * @return
 *//*

    public static boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (imgurl.startsWith("data:image/png;base64,")
                || imgurl.startsWith("data:image*/
/*;base64,") || imgurl.startsWith("data:image/jpg;base64,")
        )) {
            return true;
        }
        return false;
    }

    */

    /**
     * ??????????????????????????????
     */
    @SuppressLint("NewApi")
    public static void setBitmapFromLocal(String base64Data, ImageView imageView) {
        byte[] decodedString = Base64.getDecoder().decode(base64Data);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    // ??????????????????????????????????????????bitmap????????????
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

}
