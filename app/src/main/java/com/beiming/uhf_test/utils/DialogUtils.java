package com.beiming.uhf_test.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.dialog.ContrastBarCodeDialog;
import com.beiming.uhf_test.dialog.HintDialog;
import com.beiming.uhf_test.dialog.LoadingDialog;
import com.beiming.uhf_test.listener.OnContrastBarCodeDialogListener;
import com.beiming.uhf_test.listener.OnHintDialogClicklistener;

import java.util.List;


/**
 * Created by htj on 2020/7/10.
 */

public class DialogUtils {
    public static Dialog mProgressDialog;

    public static Dialog showProgressDialog(Context context, String content) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_progress, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.transparent_dialog);
        }
        mProgressDialog.setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView textView = (TextView) mProgressDialog.findViewById(R.id.tv_info);
        textView.setText(content);
        return mProgressDialog;
    }

    /**
     * 判断dialog是否处于打开状态
     *
     * @return
     */
    public static boolean isShow() {
        if (mProgressDialog == null) {
            return false;
        } else {
            return mProgressDialog.isShowing();
        }
    }

    public static void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private static ProgressDialog mDialog;

    /**
     * 显示dialog
     *
     * @param msg dialog中要显示的信息
     */
    public static void showDialog(Context context, String msg) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
            mDialog.setCancelable(false);
        }
        mDialog.setMessage(msg);
        mDialog.show();
    }

    /**
     * 隐藏dialog
     */
    public static void hideDialog() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    /**
     * 提示dialog
     */
    public static HintDialog showHintDialog(Context context, String title, String cancel, String confirm, OnHintDialogClicklistener onClicklistener) {
        final HintDialog hintDialog = new HintDialog(context, title, cancel, confirm, onClicklistener);
        hintDialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = hintDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;//设置高度
        lp.gravity = Gravity.CENTER;
        hintDialog.setCanceledOnTouchOutside(true);
        hintDialog.getWindow().setAttributes(lp);
        return hintDialog;
    }

    /**
     * 电表召测详情底部弹框
     */
    public static ContrastBarCodeDialog showContrastBarCodeDialog(Context context, List<MeterBean> exsitBarCode, String boxBarCode) {
        final ContrastBarCodeDialog dialog = new ContrastBarCodeDialog(context, exsitBarCode, boxBarCode);
        dialog.show();
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialog_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
        lp.height = (int) (display.getHeight() * 0.8);//设置高度
        lp.gravity = Gravity.BOTTOM;
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    //消息展示
    public static LoadingDialog showMessageDialog(Context context, String mssage, boolean cancelable, boolean cancelOutside) {
        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(context)
                .setMessage(mssage)
                .setCancelable(cancelable)//返回键是否可点击
                .setCancelOutside(cancelOutside);//窗体外是否可点击
        LoadingDialog dialog = loadBuilder.create();
        dialog.show();//显示弹窗
        return dialog;

    }
}
