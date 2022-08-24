package com.beiming.uhf_test.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.utils.pic.OtherUtils;
import com.beiming.uhf_test.view.picinput.PicConst;

/**
 */

public class GuideDlg extends Dialog implements View.OnClickListener {
    private static final float IMG_WIDTH_PER = 0.85f;       //根据设计图标注计算出图片宽度与屏幕宽度的比例
    private View rootView;
    private ImageView imageView;

    private Context mContext;
    private float wCH = 1.5f; //图片宽高比

    private int type;

    public GuideDlg(@NonNull Context context, int type) {
        super(context, R.style.Theme_dialog);

        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.guide_dlg_layout, null);
        imageView = rootView.findViewById(R.id.img);

        rootView.findViewById(R.id.close_btn).setOnClickListener(this);
        rootView.findViewById(R.id.img).setOnClickListener(this);
        rootView.findViewById(R.id.root).setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
        this.type = type;
        if (type == PicConst.TYPE_1) {
            imageView.setImageResource(R.drawable.xiang_all);
        } else if (type == PicConst.TYPE_2) {
            imageView.setImageResource(R.drawable.biao_buju);
        } else if(type == PicConst.TYPE_3){
            imageView.setImageResource(R.drawable.single_biao);
        }
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
        ViewGroup.LayoutParams imgParams = imageView.getLayoutParams();
        int imgWidth = (int) (OtherUtils.getWidthInPx(mContext) * IMG_WIDTH_PER);
        imgParams.width = imgWidth;
        imgParams.height = (int) (imgWidth * wCH);
        imageView.setLayoutParams(imgParams);
    }

    /**
     * activity是否运行中
     */
    private boolean isRunning() {
        if (mContext instanceof Activity) {
            return !((Activity) mContext).isDestroyed() && !((Activity) mContext).isFinishing();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
//                EventBus.getDefault().post(new MineCenterRedBubbleEvent()); //个人中心红点的
                dismiss();
                break;
        }
    }
}
