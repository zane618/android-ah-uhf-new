package com.beiming.uhf_test.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.beiming.uhf_test.R;


/**
 * Created by htj on 2020/7/22.
 */

public class LoadingDialog extends Dialog {


    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private Context context;
        private String message;
        private boolean isShowMessage = true;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;
        private TextView msgText;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */
        public void changeMessage(String message) {
            msgText.setText(message);
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public LoadingDialog create() {

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            LoadingDialog loadingDailog = new LoadingDialog(context, R.style.CustomDialog);
            msgText = (TextView) view.findViewById(R.id.tipTextView);
            if (isShowMessage) {
                msgText.setText(message);
            } else {
                msgText.setVisibility(View.GONE);
            }
            WindowManager.LayoutParams parms = ((Activity) context).getWindow().getAttributes();
            parms.alpha = 0.5f;//设置该值可以改变非展示区域的颜色暗亮程度
            ((Activity) context).getWindow().setAttributes(parms);
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            loadingDailog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    WindowManager.LayoutParams parms = ((Activity) context).getWindow().getAttributes();
                    parms.alpha = 1.0f;//设置该值可以改变非展示区域的颜色暗亮程度
                    ((Activity) context).getWindow().setAttributes(parms);
                }
            });
            return loadingDailog;

        }
    }
}