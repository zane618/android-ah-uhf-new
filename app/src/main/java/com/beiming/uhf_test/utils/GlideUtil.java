package com.beiming.uhf_test.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.luck.picture.lib.utils.DensityUtil;

import java.io.File;

/**
 * Glide封装
 * Created by duantianhui on 2017/8/5.
 */

public class GlideUtil {
    /**
     * 加载中及加载失败的默认图
     */
    private static final int defResId = R.mipmap.ic_launcher;

    /**
     * 图片加载默认配置
     *
     * @param loadingDef
     * @return
     */
    public static RequestOptions createOptions(int loadingDef) {
        return new RequestOptions()
                .placeholder(loadingDef)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(loadingDef);
    }

    public static RequestOptions createOptions(Drawable drawable) {
        return new RequestOptions()
                .placeholder(drawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(drawable);
    }

    public static RequestOptions createOptions() {
        return new RequestOptions()
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public static RequestOptions createNoneCacheOptions() {
        return new RequestOptions()
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    public static void load(Context context, ImageView view, String uri, int defResId) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).load(uri).apply(createOptions(defResId)).into(view);
    }

    /**
     *
     * @param id 占位图资源id，不传参使用默认占位图
     */
    public static void load(Context context, ImageView view, String uri, int ...id) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).load(uri).apply(id != null && id.length > 0 ? createOptions(id[0]) : createOptions()).into(view);
    }

    public static void load(Context context, ImageView view, String uri, Drawable drawables) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).load(uri).apply(drawables != null ? createOptions(drawables) : createOptions()).into(view);
    }

    public static void load(Context context, ImageView view, int resId) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).load(resId).apply(createOptions()).into(view);
    }

    /**
     * 加载图片，指定尺寸，避免大图耗内存
     * @param context
     * @param view
     * @param uri
     * @param width
     * @param height
     * @param defResId
     */
    public static void load(Context context, ImageView view, String uri, int width, int height, int defResId){
        if(isActivityDestroyed(context)) return;

        RequestOptions options = createOptions(defResId);
        options.override(width, height);
        Glide.with(context).load(uri).apply(options).into(view);
    }

    public static void loadGif(final Context context, final ImageView view, String uri, int defResId) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).asGif().load(uri).apply(createOptions(defResId)).into(view);
    }

    /**
     * 加载图片，默认图片使用 global_def_pic
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPic(Context context, ImageView view, String uri) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).load(uri).apply(createOptions(R.mipmap.ic_launcher)).into(view);
    }

    /**
     * 加载图片，加载中和加载失败无默认图的场景
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPicNoDef(Context context, ImageView view, String uri) {
        if(isActivityDestroyed(context)) return;

        Glide.with(context).load(uri).into(view);
    }

    /**
     * 加载图片，默认图为大的长方形
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPicDefBigRect(Context context, ImageView view, String uri) {
        if(isActivityDestroyed(context)) return;

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .override(DensityUtil.dip2px(context, 340), DensityUtil.dip2px(context, 170));        //打长方形， 约：宽340dp，高170dp

        Glide.with(context).load(uri).apply(options).into(view);
    }

    /**
     * 加载图片，默认图为大的长方形
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPicDefBigRectBySize(Context context, ImageView view, String uri) {
        if(isActivityDestroyed(context)) return;

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

//        LogPrintUtil.thduan("loadPicDefBigRect: " + width + ", " + height);
        if(width > 0 && height > 0) {
            options.override(width, height);
        }

        Glide.with(context).load(uri).apply(options).into(view);
    }

    /**
     * 加载图片，默认图为大的长方形
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPicDefBigRect(Context context, ImageView view, String uri, int width, int height) {
        if(isActivityDestroyed(context)) return;

        LogPrintUtil.zhangshi("loadPicDefBigRect: " + width + ", " + height);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        if(width > 0 && height > 0) {
            options.override(width, height);
        }

        Glide.with(context).load(uri).apply(options).into(view);
    }

    /**
     * 加载图片，默认图为小的长方形
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPicDefSmallRect(Context context, ImageView view, String uri) {
        if(isActivityDestroyed(context)) return;

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .override(DensityUtil.dip2px(context, 100), DensityUtil.dip2px(context, 70));        //小长方形， 约：宽100dp，高70dp

        Glide.with(context).load(uri).apply(options).into(view);
    }

    /**
     * 加载图片，默认图为正方形
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void loadPicDefSquare(Context context, ImageView view, String uri) {
        if(isActivityDestroyed(context)) return;

        RequestOptions options = new RequestOptions()
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.mipmap.ic_launcher)
                                    .override(DensityUtil.dip2px(context, 90));        //正方形大约90dp
        Glide.with(context).load(uri).apply(options).into(view);
    }

    /**
     * 加载图片到 target
     * @param context
     * @param url
     * @param simpleTarget
     */
    public static void loadToTarget(Context context, String url, SimpleTarget simpleTarget) {
        if(isActivityDestroyed(context)) return;
        Glide.with(context).load(url).into(simpleTarget);
    }

    /**
     * 预加载
     * @param uri
     */
    public static void preLoad(final String uri) {
        Glide.with(App.getInstance()).load(uri).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                LogPrintUtil.zhangshi("onLoadFailed: " + e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                LogPrintUtil.zhangshi("onResourceReady: " + isFirstResource);
                return false;
            }
        }).preload();
    }

    /**
     * 仅仅是下载
     * @param uri
     * @param listener
     */
    public static void downloadOnly(final String uri, final RequestListener listener) {
        Glide.with(App.getInstance()).downloadOnly().load(uri).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                LogPrintUtil.zhangshi("onLoadFailed: " + uri);
                if(listener != null) {
                    listener.onLoadFailed(e, model, target, isFirstResource);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                LogPrintUtil.zhangshi("onResourceReady: " + uri);
                if(listener != null) {
                    listener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                }
                return false;
            }
        });
    }

    /**
     * 判断activity是否 destroy了，如果destroy了，则不要继续加载图片
     * @param context
     * @return
     */
    private static boolean isActivityDestroyed(Context context) {
        if(context != null) {
            if(context instanceof  Activity) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return ((Activity)context).isDestroyed();
                }else {
                    return ((Activity)context).isFinishing();
                }
            }else {
                return false;
            }
        }else {
            return true;
        }
    }

    public interface LoadBack {
        //加载结束
        void finish();
    }
}
