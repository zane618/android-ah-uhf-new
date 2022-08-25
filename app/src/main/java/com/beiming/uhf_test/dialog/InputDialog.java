package com.beiming.uhf_test.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.utils.ToastUtils;
import com.beiming.uhf_test.utils.pic.OtherUtils;
import com.beiming.uhf_test.view.picinput.PicConst;

/**
 */

public class InputDialog extends Dialog {
    private View rootView;

    private Context mContext;
    private IListener listener;
    private EditText et;

    public InputDialog(@NonNull Context context, IListener listener) {
        super(context, R.style.Theme_dialog);

        mContext = context;
        this.listener = listener;
        rootView = LayoutInflater.from(context).inflate(R.layout.input_dlg_layout, null);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        et = rootView.findViewById(R.id.et);

        rootView.findViewById(R.id.tv_cancel).setOnClickListener(view -> {
            dismiss();
        });
        rootView.findViewById(R.id.tv_sure).setOnClickListener(view -> {
            String s = et.getText().toString();
            if (TextUtils.isEmpty(s)) {
                ToastUtils.showToast("请输入备注内容");
            } else {
                if (listener != null) {
                    listener.onNoteBack(s);
                }
                dismiss();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(rootView);
        Window window = getWindow();
        if (window != null) {
//            window.setWindowAnimations(R.style.Ad_Dlg_Anim_Style);
            /**
             * 屏幕宽度适配
             */
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = OtherUtils.getWidthInPx(mContext);
            params.height = OtherUtils.getHeightInPx(mContext);
            getWindow().setAttributes(params);
        }

        new Handler().postDelayed(() -> showKeyboard(), 800);
    }

    public interface IListener {
        void onNoteBack(String note);
    }

    public void showKeyboard() {
        if(et!=null){
            //设置可获得焦点
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            //请求获得焦点
            et.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) et
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
        }
    }

}
