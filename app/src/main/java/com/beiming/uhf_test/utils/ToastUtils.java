package com.beiming.uhf_test.utils;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.beiming.uhf_test.App;


/**
 * 作者： itheima
 * 时间：2016-10-15 16:23
 * 网址：http://www.itheima.com
 */

public class ToastUtils {

    private static Toast toast;

    public static void showToast(Context context, String msg){

        try {
            if (null == toast) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                View view = toast.getView();
                toast.cancel();
                toast = new Toast(context);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setText(msg);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
            Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

        /*if (sToast==null){
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        sToast.setText(msg);
        sToast.show();*/
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast( String msg){

        try {
            if (null == toast) {
                toast = Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT);
            } else {
                View view = toast.getView();
                toast.cancel();
                toast = new Toast(App.getContext());
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setText(msg);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
            Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    /**
     * 弹出长时间吐司
     * @param mContext
     * @param content
     */
    public static void showToastForLong(Context mContext , String content){
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
