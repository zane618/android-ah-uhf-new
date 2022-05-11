package com.beiming.uhf_test.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2015-03-10.
 */
public abstract class KeyDwonFragment extends Fragment {
    /**
     * 封装Toast对象
     */
    private static Toast toast;
    public Context context;
    public boolean isFirstVisible = true;//是否第一次加载

    public void myOnKeyDwon() {

    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        context = ctx;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null && isFirstVisible) {
                initData();
                isFirstVisible = false;
            } else {
            }
        }
    }

    /**
     * 初始化、绑定数据
     *
     */
    protected abstract void initData();

    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
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
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
//            Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
}
